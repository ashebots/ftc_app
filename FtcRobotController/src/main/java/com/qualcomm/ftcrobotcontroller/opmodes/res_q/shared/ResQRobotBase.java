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
    public Servo servoArm; //Temporary servo arm to dump climbers. CONTINUOUS

    public Servo servoLeverHitterLeft; //Refers to left "drive side"
    public Servo servoLeverHitterRight; //Refers to right "drive side"

    public Servo servoPlowLeft; //Refers to left "drive side"
    public Servo servoPlowRight; //Refers to right "drive side"



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


        //motorArm = new Motor(hardwareMap.dcMotor.get("armMotor")); //DISABLED DUE TO HARDWARE
        servoArm = hardwareMap.servo.get("servoArm");
        servoArm.setDirection(Servo.Direction.REVERSE);


        servoLeverHitterLeft = hardwareMap.servo.get("leverHitterL");
        servoLeverHitterLeft.setDirection(Servo.Direction.REVERSE); //Should be that 0 is down //Unsure which should be reversed
        servoLeverHitterRight = hardwareMap.servo.get("leverHitterR");

        servoPlowLeft = hardwareMap.servo.get("plowL");
        servoPlowLeft.setDirection(Servo.Direction.REVERSE); //Should be that 0 is down //Unsure which should be reversed
        servoPlowRight = hardwareMap.servo.get("plowR");

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
