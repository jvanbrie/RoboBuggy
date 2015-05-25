package com.roboclub.robobuggy.localization;

/***
 * 
 * @author trevor decker
 *
 * WARNING euler angles can reach a singularity when the y axis rotation reaches +- 90 degrees. 
 *  This is commonly known as gimble lock, to avoid the problem use RotationMatrices or Quaternions 
 */
public class EulerAngles {
	private double roll;
	private double pitch;
	private double yaw;
	
/***
 * Constructor for a euler angle, assumes that roll and pitch are both 0
 * @param yaw
 */
	public EulerAngles(double yaw){
		//roll and pitch assumed to be 0 
		this.yaw = yaw;
		this.roll = 0;
		this.pitch = 0;
	}
	
	/***
	 * Constructor for a representation of euler angles 
	 * @param roll
	 * @param pitch
	 * @param yaw
	 */
	public EulerAngles(double roll,double pitch,double yaw){
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	/***
	 * returns the value of the roll for this euler angle set
	 * @return
	 */
	public double getRoll(){
		return this.roll;
	}
	
	/***
	 * returns the value of the pitch for this euler angle set
	 * @return
	 */
	public double getPitch(){
		return this.pitch;
	}
	
	/***
	 * returns the value for the yaw for this euler angle set
	 * @return
	 */
	public double getYaw(){
		return this.yaw;
	}
	
	/***
	 * sets the value for the roll for this euler angle set
	 * @param newRoll
	 */
	public void setRoll(double newRoll){
		this.roll = newRoll;
	}
	
	/***
	 * sets the value for the pitch for this euler angle set
	 * @param newPitch
	 */
	public void setPitch(double newPitch){
		this.pitch = newPitch;
	}
	
	/***
	 * sets the value for the yaw for thsi euler angle set
	 * @param newYaw
	 */
	public void setYaw(double newYaw){
		this.yaw = newYaw;
	}
	
	public boolean inSingularity(){
		return true;
		//TODO
	}
	
	public void setInSingularity(){
		//TODO
	}
	
	public RotationMatrix_SO2 toRotationMatrix_SO2(){
		return null;
		//TODO
	}
	
	public RotationMatrix_SO3 toRotationMatrix_SO3(){
		return null;
		//TODO
	}
	
	public Quaternion toQuaternion(){
		return null;
		//TODO
	}
	
	
}
