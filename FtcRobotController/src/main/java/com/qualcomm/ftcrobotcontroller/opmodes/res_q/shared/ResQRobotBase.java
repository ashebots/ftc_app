package com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared;


import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
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

    public Motor motorLockingBar;

    public Servo servoClimberDumper;

    public Servo servoPlow;

    public Servo servoAllClearLeft;
    public Servo servoAllClearRight;

    public Servo servoLeverHitterLeft; //Refers to left "drive side"
    public Servo servoLeverHitterRight; //Refers to right "drive side"



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

        // SR COMPETITION TESTING
        motorDriveLeft.setPower(0);
        motorDriveRight.setPower(0);

        //END SR COMP


        motorArm = new Motor(hardwareMap.dcMotor.get("armMotor"));
        motorArm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


        motorLockingBar = new Motor(hardwareMap.dcMotor.get("motorLockingBar"));


        servoClimberDumper = hardwareMap.servo.get("climberDumper"); //continuous servo

        servoPlow = hardwareMap.servo.get("plow"); //continuous servo. < 0.5 is down, > 0.5 is up

        servoAllClearLeft = hardwareMap.servo.get("allClearL");
        servoAllClearLeft.setDirection(Servo.Direction.REVERSE);
        servoAllClearRight = hardwareMap.servo.get("allClearR");


        servoLeverHitterLeft = hardwareMap.servo.get("leverHitterL");
        servoLeverHitterLeft.setDirection(Servo.Direction.REVERSE); //Should be that 0 is down //Unsure which should be reversed
        servoLeverHitterRight = hardwareMap.servo.get("leverHitterR");
    }

    public double easeInCirc(double currentInput, double startOutput, double endOutput, double endInput) {
        currentInput /= endInput;
        return -endOutput * (Math.sqrt(1 - currentInput*currentInput) - 1) + startOutput;
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
