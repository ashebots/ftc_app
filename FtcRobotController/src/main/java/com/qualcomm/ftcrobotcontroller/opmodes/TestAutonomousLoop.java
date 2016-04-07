package com.qualcomm.ftcrobotcontroller.opmodes;

//import required things
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;

import com.qualcomm.ftcrobotcontroller.opmodes.BNO055LIB;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Owner on 8/31/201c5.
 */
public class TestAutonomousLoop extends OpMode {
    //define hardware
    DcMotor motorLeft;
    DcMotor motorRight;
    BNO055LIB imu;
    Servo srv;;
    //define software
    OrderedBoolChecker blCheck;
    AutoChassis chassis;
    AutoServo atServo;
    String s;
    @Override
    public void init() {
        //initializes hardware
        motorLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorRight = hardwareMap.dcMotor.get("motorDriveRight");
        srv = hardwareMap.servo.get("plow");
        try {
            imu = new BNO055LIB(hardwareMap, "bno055"
                    , (byte)(BNO055LIB.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    //addressing
                    , (byte)BNO055LIB.OPERATION_MODE_IMU);
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception: " + e.getMessage());
        }
        blCheck = new OrderedBoolChecker();
        //sets preferences for hardware
        chassis = new AutoChassis(motorLeft, motorRight, imu);
        atServo = new AutoServo(srv);
    }
    @Override
    public void start() {
        chassis.resetEncs();
    }
    @Override
    public void loop() {
        //updates encoders and bno
        chassis.getValues();
        atServo.getValues();
        //outputs step number
        telemetry.addData("Executing step",blCheck.getStep()+1);
        telemetry.addData("BNO",chassis.angle());
        telemetry.addData("Encoder  Left",chassis.encoderLeft);
        telemetry.addData("Encoder Right",chassis.encoderRight);
        telemetry.addData("Boolean A",chassis.aRange(-20,15));
        telemetry.addData("Boolean B",chassis.aRange(20,15));
        telemetry.addData("Servo Position",atServo.position);
        s = "Idle";
        //executes the action specified by the step number, as well as checking if it should move to the next step
        switch(blCheck.getStep()+1) {
                   case 1:
                s = chassis.straightMotors(-1,0.1);
                blCheck.ifBool(chassis.mRange(3500,150));
            break; case 2:
                s = chassis.turnMotors(-1);
                blCheck.ifBool(chassis.aRange(-40,5));
                chassis.resetEncs();
                chassis.setStandard(-45);
            break; case 3:
                s = chassis.straightMotors(-1,0.1);
                blCheck.ifBool(chassis.mRange(3500,150));
            break; case 4:
                s = atServo.setServo(0.3);
                blCheck.ifBool(atServo.sRange(100,15));
            break; case 5:
                s = chassis.turnMotors(-1);
                blCheck.ifBool(chassis.aRange(-130,5));
                chassis.resetEncs();
            break; default:
                chassis.setMotors(0);
            break;
        }
        telemetry.addData("Status",s);
    }
    @Override
    //shuts motors off
    public void stop() {
        chassis.setMotors(0);
    }
}