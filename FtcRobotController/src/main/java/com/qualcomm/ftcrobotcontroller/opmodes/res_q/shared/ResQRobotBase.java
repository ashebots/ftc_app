package com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared;


import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.ashebots.ftcandroidlib.motor.Motor;

import java.lang.reflect.Method;

public abstract class ResQRobotBase extends OpMode
{
    public Context appContext; //Android "Context" of RC app
    public SensorManager sensorManager; //Used for Android sensors

    public Motor motorDriveLeft;
    public Motor motorDriveRight;

    public Motor motorArm; //Single motor to control to arm thingy

    public Servo servoLeverHitterLeft; //Refers to left "drive side"
    public Servo servoLeverHitterRight; //Refers to right "drive side"

    public Servo servoPlowLeft; //Refers to left "drive side"
    public Servo servoPlowRight; //Refers to right "drive side"




    public enum AllianceColor {
        UNKNOWN,
        BLUE,
        RED
    }


    @Override
    public void init()
    {
        /*
        appContext = getContext();
        sensorManager = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = new AccelerometerSensor(appContext, sensorManager, SensorManager.SENSOR_DELAY_FASTEST, 7.0f);
        */
        motorDriveLeft = new Motor(hardwareMap.dcMotor.get("motorDriveLeft"));
        motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRight = new Motor(hardwareMap.dcMotor.get("motorDriveRight"));


        motorArm = new Motor(hardwareMap.dcMotor.get("armMotor"));

        /*
        servoLeverHitterLeft = hardwareMap.servo.get("leverHitterL");
        servoLeverHitterRight = hardwareMap.servo.get("leverHitterR");

        servoPlowLeft = hardwareMap.servo.get("plowL");
        servoPlowRight = hardwareMap.servo.get("plowR");
        */
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
