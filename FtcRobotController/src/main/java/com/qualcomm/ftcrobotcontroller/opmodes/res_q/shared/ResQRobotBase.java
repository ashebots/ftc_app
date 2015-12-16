package com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared;


import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.ashebots.ftcandroidlib.motor.Motor;

import java.lang.reflect.Method;

public abstract class ResQRobotBase extends OpMode
{
    public Context appContext; //Android "Context" of RC app
    public SensorManager sensorManager; //Used for Android sensors

    public AccelerometerSensor sensorAccelerometer;

    public Motor motorDriveLeft;
    public Motor motorDriveRight;

    public Motor motorArmJoint1; //First arm joint, one closest to robot
    public Motor motorArmJoint2; //Second arm join, one farthest from robot
    public Motor motorArmSwivel; //Motor that turns the arm's lazy-susan base

    //public Motor motorBoxSweeper;
    public Servo servoBoxSweeper;
    public float boxSweeperDelta = 0.01f;
    public float boxSweeperPos = 0f;
    public float boxSweeperMinRange = 0.0f;
    public float boxSweeperMaxRange = 1.0f;

    public TouchSensor sensorTouchArmJoint1; //Sensor to detect when the first arm join is fully closed
    public TouchSensor sensorTouchArmJoint2; //Sensor to detect when the second arm join is fully closed


    @Override
    public void init()
    {
        /*
        appContext = getContext();
        sensorManager = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = new AccelerometerSensor(appContext, sensorManager, SensorManager.SENSOR_DELAY_FASTEST, 7.0f);
        */

        motorDriveLeft = new Motor(hardwareMap.dcMotor.get("motorDriveLeft"));
        motorDriveRight = new Motor(hardwareMap.dcMotor.get("motorDriveRight"));
        motorDriveRight.setDirection(DcMotor.Direction.REVERSE);

        motorArmJoint1 = new Motor(hardwareMap.dcMotor.get("motorArmJoint1"));
        motorArmJoint1.setDirection(DcMotor.Direction.REVERSE);
        motorArmJoint2 = new Motor(hardwareMap.dcMotor.get("motorArmJoint2"));
        motorArmJoint2.setDirection(DcMotor.Direction.REVERSE);
        motorArmSwivel = new Motor(hardwareMap.dcMotor.get("motorArmSwivel"));

        //motorBoxSweeper = new Motor(hardwareMap.dcMotor.get("motorBoxSweeper"));
        servoBoxSweeper = hardwareMap.servo.get("servoBoxSweeper");

        sensorTouchArmJoint1 = hardwareMap.touchSensor.get("sensorTouchArmJoint1");
        sensorTouchArmJoint2 = hardwareMap.touchSensor.get("sensorTouchArmJoint2");
    }



    //Taken from LASA Robotics
    //Gets the Android "context" of the RC app
    public static Context getContext() {
        try {
            final Class<?> activityThreadClass =
                    Class.forName("android.app.ActivityThread");
            //find and load the main activity method
            final Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (final Throwable e) {
            // handle exception
            return null;
        }
    }

}
