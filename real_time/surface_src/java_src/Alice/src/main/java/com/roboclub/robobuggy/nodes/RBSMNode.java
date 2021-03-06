package com.roboclub.robobuggy.nodes;

import java.util.Date;

import gnu.io.SerialPort;

import com.orsoncharts.util.json.JSONObject;
import com.roboclub.robobuggy.messages.EncoderMeasurement;
import com.roboclub.robobuggy.messages.StateMessage;
import com.roboclub.robobuggy.messages.SteeringMeasurement;
import com.roboclub.robobuggy.messages.BrakeControlMessage;
import com.roboclub.robobuggy.messages.DriveControlMessage;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.messages.FingerPrintMessage;
import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.sensors.SensorState;
import com.roboclub.robobuggy.serial.RBPair;
import com.roboclub.robobuggy.serial.RBSerial;
import com.roboclub.robobuggy.serial.RBSerialMessage;
import com.roboclub.robobuggy.main.MessageLevel;
import com.roboclub.robobuggy.main.RobobuggyLogicException;

/**
 * @author Trevor Decker
 * @author Matt Sebek
 * @author Kevin Brennan
 *
 * CHANGELOG: NONE
 * 
 * DESCRIPTION: node for talking to the the low level controller via rbsm
 */

public class RBSMNode extends PeriodicSerialNode implements Node 
{
	private static final double TICKS_PER_REV = 7.0;
	
	// Measured as 2 feet. Though could be made more precise. 
	private static final double M_PER_REV = 0.61;
	
	/** Steering Angle Conversion Rate */
	private final int ARD_TO_DEG = 100;
	/** Steering Angle offset?? */
	private final int OFFSET = -200;

	//Stored commanded values
	private short commandedAngle = 0;
	private boolean commandedBrakeEngaged = true;

	// accumulated
	private int encTicks = 0;
	private int potValue = -1;
	private int steeringAngle = 0;
	
	// last state
	private double accDistLast = 0.0;
	private double instVelocityLast = 0.0;
	private Date timeLast = new Date();
	
	Publisher messagePub_enc;
	Publisher messagePub_pot;
	Publisher messagePub_controllerSteering;
	Publisher brakePub; 
	
	Publisher statePub_enc;
	Publisher statePub_pot;
	
	Subscriber messageSub_angle;
	Subscriber messageSub_brakes;

	Publisher messagePub_fp; //Fingerprint for low level hash
	
	/**
	 * Construct a new RBSMNode object
	 * @param sensor_enc channel the encoder is on
	 * @param sensor_pot channel the potentiometer is on
	 * @param period the period at which control messages are sent to the Arduino
	 */
	public RBSMNode(SensorChannel sensor_enc, SensorChannel sensor_pot, int period) 
	{
		super("ENCODER", period);
		//messagePubs forward exact information received from arduino
		messagePub_enc = new Publisher(sensor_enc.getMsgPath());
		messagePub_pot = new Publisher(sensor_pot.getMsgPath());
		messagePub_controllerSteering = new Publisher(SensorChannel.STEERING_COMMANDED.getMsgPath());
		brakePub = new Publisher(SensorChannel.BRAKE.getMsgPath());
		messagePub_fp = new Publisher(SensorChannel.FP_HASH.getMsgPath());

		//statePub forwards this node's estimate of the state
		//(i.e. after filtering bad data)
		statePub_enc = new Publisher(sensor_enc.getStatePath());
		statePub_pot = new Publisher(sensor_pot.getStatePath());

		//Initialize publishers to indicate the sensor is disconnected
		statePub_enc.publish(new StateMessage(SensorState.DISCONNECTED));
		statePub_pot.publish(new StateMessage(SensorState.DISCONNECTED));
		
		//Initialize subscribers to commanded angle and brakes state
		messageSub_angle = new Subscriber(SensorChannel.DRIVE_CTRL.getMsgPath(),
				new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				commandedAngle = ((DriveControlMessage)m).getAngleShort();
			}
		});
		messageSub_brakes = new Subscriber(SensorChannel.BRAKE_CTRL.getMsgPath(),
				new MessageListener() {
			@Override
			public void actionPerformed(String topicName, Message m) {
				commandedBrakeEngaged = ((BrakeControlMessage)m).isBrakeEngaged();
			}
		});
		
		System.out.println("rbsmnode start");
	}
	
	/**{@inheritDoc}*/
	@Override
	public void setSerialPort(SerialPort sp) 
	{
		super.setSerialPort(sp);
		statePub_enc.publish(new StateMessage(SensorState.ON));
		statePub_pot.publish(new StateMessage(SensorState.ON));
	}
	
	/**
	 * Calculates an estimate of the velocity from an encoder measurement
	 * @param dataWord raw data bits received from the RBSM message
	 * @return an EncoderMeasurement object representing the velocity
	 */
	private EncoderMeasurement estimateVelocity(int dataWord) 
	{
		Date currTime = new Date();
		double accDist = ((double)(encTicks)) * M_PER_REV / TICKS_PER_REV;
		double instVelocity = (accDist - accDistLast) * 1000 / (currTime.getTime() - timeLast.getTime());
		double instAccel = (instVelocity - instVelocityLast) * 1000 / (currTime.getTime() - timeLast.getTime());
		accDistLast = accDist;
		instVelocityLast = instVelocity;
		timeLast = currTime;
		
		return new EncoderMeasurement(currTime, dataWord, accDist, instVelocity, instAccel);
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean matchDataSample(byte[] sample) 
	{
		// Peel what ever is not a message
		
		// Check that whatever we give it is a message
		
		return true;
	}

	/**{@inheritDoc}*/
	@Override
	public int matchDataMinSize() 
	{
		return 2*RBSerial.MSG_LEN;
	}

	/**{@inheritDoc}*/
	@Override
	//must be the same as the baud rate of the arduino 
	public int baudRate() 
	{
		return 115200;
	}

	/**{@inheritDoc}*/
	@Override
	public int peel(byte[] buffer, int start, int bytes_available) 
	{
		// The Encoder sends 3 types of messages
		//  - Encoder ticks since last message (keep)
		//  - Number of ticks since last reset
		//  - Timestamp since reset
		RBPair rbp = RBSerial.peel(buffer, start, bytes_available);
		switch(rbp.getNumberOfBytesRead()) 
		{
			case 0:
				return 0;
			case 1: 
				return 1;
			case 6: 
				break;
			default: 
			{
				System.out.println("HOW DID NOT A SIX GET HERE");
				//TODO add error
				break;
			}
		
		}
		
		RBSerialMessage message = rbp.getMessage();
		byte headerByte = message.getHeaderByte();
		switch (headerByte)
		{
			case RBSerialMessage.ENC_TICK_SINCE_RESET:
				// This is a delta-distance! Do a thing!
				encTicks = message.getDataWord() & 0xFFFF;
				messagePub_enc.publish(estimateVelocity(message.getDataWord()));
				System.out.println(encTicks);
				break;
			case RBSerialMessage.RBSM_MID_MEGA_STEER_FEEDBACK:
				// This is a delta-distance! Do a thing!
				potValue = message.getDataWord();
				System.out.println(potValue);
				messagePub_pot.publish(new SteeringMeasurement(-(potValue + OFFSET)/ARD_TO_DEG));
				break;
			case RBSerialMessage.RBSM_MID_MEGA_STEER_ANGLE:
				steeringAngle = message.getDataWord();
				messagePub_controllerSteering.publish(new SteeringMeasurement(steeringAngle));
				break;
			case RBSerialMessage.FP_HASH:
				System.out.println(message.getDataWord());
				messagePub_fp.publish(new FingerPrintMessage(message.getDataWord()));
				break;
			default: //Unhandled or invalid RBSM message header was received.
				new RobobuggyLogicException("Invalid RBSM message header\n", MessageLevel.NOTE);
				break;
		}

		
		return 6;
	}

	/**
	 * Converts a string message into a JSON object
	 * @param message String message
	 * @return JSON object representing the string message
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject translatePeelMessageToJObject(String message) 
	{
		String[] messageData = message.split(",");
		String sensorName = messageData[0];
		sensorName = sensorName.substring(sensorName.indexOf("/") + 1);
		JSONObject data = new JSONObject();
		JSONObject params = new JSONObject();
		
		data.put("timestamp", messageData[1]);
		
		if(sensorName.equals("steering") || sensorName.equals("commanded_steering")) 
		{
			data.put("name", "Steering_" + sensorName);
			params.put("angle", Float.valueOf(messageData[2]));
		}
		else if (sensorName.equals("fp_hash")) {
			data.put("name", "Fingerprint Hash");
			params.put("hash_value", messageData[2]);
			
		}
		else if (sensorName.equals("encoder")) 
		{
			data.put("name", "Encoder");
			params.put("dataword", Double.valueOf(messageData[2]));
			params.put("distance", Double.valueOf(messageData[3]));
			params.put("velocity", Double.valueOf(messageData[4]));
			params.put("acceleration", Double.valueOf(messageData[5]));
		}
		else 
		{
			System.err.println("WAT " + sensorName);
		}
		
		data.put("params", params);
		// TODO Auto-generated method stub
		return data;
	}

	/**
	 * Used to send the commanded angle and brake state to the Arduino
	 */
	@Override
	protected void update() {
		RBSMessage msg = new RBSMessage(commandedAngle, commandedBrakeEngaged);
		send(msg.getMessageBytes());
	}
	
	/**
	 * Private class used to represent RBSM messages
	 */
	private class RBSMessage {
		
		private static final byte HEADER = RBSerialMessage.RBSM_MID_MEGA_COMMAND;
		private static final byte FOOTER = RBSerialMessage.FOOTER;
		
		private short angle;
		private boolean brakesEngaged;
		
		/**
		 * Create a new RBSMessage object to send to the Arduino
		 * @param angle desired commanded angle in degrees*1000
		 * @param brakesEngaged desired brake state
		 */
		private RBSMessage(short angle, boolean brakesEngaged) {
			this.angle = angle;
			this.brakesEngaged = brakesEngaged;
		}
		
		/**
		 * Returns a byte array of the RBSMessage to send to the Arduino
		 * @return a byte array of the RBSMessage to send to the Arduino
		 */
		private byte[] getMessageBytes() {
			byte[] bytes = new byte[6];
			bytes[0] = HEADER;
			bytes[1] = (byte)((angle >> 8)&0xFF);
			bytes[2] = (byte)(angle);
			bytes[3] = (byte)((brakesEngaged)?1:0);
			bytes[4] = (byte)(0xc0);
			bytes[5] = FOOTER;
			return bytes;
		}
	}

}


