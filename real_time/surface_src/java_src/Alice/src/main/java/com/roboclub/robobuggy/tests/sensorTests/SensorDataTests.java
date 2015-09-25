package com.roboclub.robobuggy.tests.sensorTests;

import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by vivaanbahl on 9/24/15.
 */
public class SensorDataTests {
    private Vector<Subscriber> subscribers;
    private MessageListener listener;
    private int dataPointsReceivedSinceLastCheck;

    private boolean failedTest;
    private String testName;

    public void setUp() {
        System.out.println("Beginning test " + testName);
        failedTest = false;
        listener = new MessageListener() {

            @Override
            public void actionPerformed(String topicName, Message m) {
                System.out.println("Message received!");
                dataPointsReceivedSinceLastCheck++;
            }
        };
        subscribers = new Vector<Subscriber>();
    }

    public void tearDown() {
        if (failedTest) {
            System.err.println("Failed test " + testName + " !");
        }
        listener = null;
        subscribers = new Vector<Subscriber>();
    }

    public void test_standardIn_encoder() throws InterruptedException {
        test_standardIn_generic(SensorChannel.ENCODER.toString());
    }

    public void test_standardIn_gps() throws InterruptedException {
        test_standardIn_generic(SensorChannel.GPS.toString());
    }

    public void test_standardIn_imu() throws InterruptedException {
        test_standardIn_generic(SensorChannel.IMU.toString());
    }

    public void test_standardIn_brake() throws InterruptedException {
        test_standardIn_generic(SensorChannel.BRAKE.toString());
    }

    public void test_standardIn_steering() throws InterruptedException {
        test_standardIn_generic(SensorChannel.STEERING.toString());
    }

    public void test_standardIn_generic(String topic) throws InterruptedException {

        testName = "Standard IO : " + topic;
        setUp();

        subscribers.add(new Subscriber(topic, listener));

        Thread.sleep(1000);

        failedTest = dataPointsReceivedSinceLastCheck > 0;

        tearDown();
    }


    public void test_getContinuousData_encoder() {
        test_gettingContinuousData_generic(SensorChannel.ENCODER.toString());
    }

    public void test_getContinuousData_gps() {
        test_gettingContinuousData_generic(SensorChannel.GPS.toString());
    }

    public void test_getContinuousData_imu() {
        test_gettingContinuousData_generic(SensorChannel.IMU.toString());
    }

    public void test_getContinuousData_brake() {
        test_gettingContinuousData_generic(SensorChannel.BRAKE.toString());
    }

    public void test_getContinuousData_steering() {
        test_gettingContinuousData_generic(SensorChannel.STEERING.toString());
    }

    public void test_gettingContinuousData_generic(String topic) {
        testName = "continuous data stream - " + topic;
        setUp();

        subscribers.add(new Subscriber(topic, listener));

        final Timer timer = new Timer("Timer");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (dataPointsReceivedSinceLastCheck > 0) {
                    //yay, there's still data coming in!
                    dataPointsReceivedSinceLastCheck = 0;
                }
                else {
                    timer.cancel();
                    failedTest = true;
                }
            }
        }, 0, 100);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer.cancel();
                tearDown();
            }
        }).start();

    }

}
