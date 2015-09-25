package com.roboclub.robobuggy.tests.sensorTests;

import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.SensorChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.sun.xml.internal.xsom.impl.parser.SubstGroupBaseTypeRef;

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
        failedTest = false;
        listener = new MessageListener() {
            @Override
            public void actionPerformed(String topicName, Message m) {
                System.out.println("received message!");
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

    public void test_gettingContinuousData() {
        testName = "gettingContinuousData";
        setUp();


        listener = new MessageListener() {
            @Override
            public void actionPerformed(String topicName, Message m) {
                dataPointsReceivedSinceLastCheck++;
            }
        };
        subscribers.add(new Subscriber(SensorChannel.ENCODER.toString(), listener));

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
