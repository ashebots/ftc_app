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
public class MountainBlue extends OpMode {
    //define hardware
    DcMotor motorLeft;
    DcMotor motorRight;
    BNO055LIB imu;

    Servo plow;
    Servo lH1;
    Servo lH2;
    Servo cs1;
    Servo cs2;
    Servo cs3;
    //define software
    OrderedBoolChecker blCheck;
    AutoChassis chassis;
    AutoServo atServo;
    TimeManager timer;
    String s;
    @Override
    public void init() {
        //initializes hardware
        motorLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorRight = hardwareMap.dcMotor.get("motorDriveRight");
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

        plow = hardwareMap.servo.get("plow");
        lH1 = hardwareMap.servo.get("leverHitterL");
        lH2 = hardwareMap.servo.get("leverHitterR");
        cs1 = hardwareMap.servo.get("allClearL");
        cs2 = hardwareMap.servo.get("allClearR");
        cs3 = hardwareMap.servo.get("climberDumper");
        lH1.setDirection(Servo.Direction.REVERSE);
        plow.setPosition(0.5);
        lH1.setPosition(0.99);
        lH2.setPosition(0.99);
        cs3.setPosition(0.5);
        cs2.setPosition(0.5);
        cs1.setPosition(0.5);
        atServo = new AutoServo(plow);
        timer = new TimeManager();
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
        timer.update();
        //outputs step number
        telemetry.addData("Executing step",blCheck.getStep()+1);
        telemetry.addData("BNO",chassis.angle());
        telemetry.addData("Encoder  Left",chassis.encoderLeft);
        telemetry.addData("Encoder Right",chassis.encoderRight);
        telemetry.addData("Plow Servo Pos",atServo.position);
        telemetry.addData("Boolean A",chassis.aRange(-30-chassis.aStand,25));
        telemetry.addData("Boolean B",chassis.aRange(30-chassis.aStand,25));
        s = "Idle";
        //action to execute after step switches
        if (blCheck.ifMoved()) {chassis.stop();atServo.setServo(0.5);}
        //executes the action specified by the step number, as well as checking if it should move to the next step
        switch(blCheck.getStep()+1) {
                   case 1: //move forward
                s = chassis.straightMotors(-1,0.1,5);
                blCheck.ifBool(chassis.mRange(1500,150));
            break; case 2: //turn to plow
                s = chassis.turnMotors(-1);
                blCheck.ifBool(chassis.aRange(-40,5));
                chassis.resetEncs();
                chassis.setStandard(-45);
            break; case 3: //plow
                s = chassis.straightMotors(-1,0.1,5);
                blCheck.ifBool(chassis.mRange(5500,150));
                timer.resetTimer();
            break; case 4: //reset encoders
                chassis.stop();
                chassis.resetEncs();
                blCheck.ifBool(timer.tRange(500,25));
            break; case 5: //move back
                s = chassis.straightMotors(1,0.1,5);
                blCheck.ifBool(chassis.mRange(1250,150));
            break; case 6: //turn to face mountain
                s = chassis.turnMotors(-1);
                blCheck.ifBool(chassis.aRange(-130,5));
                chassis.resetEncs();
                timer.resetTimer();
            break; case 7: //raise plow
                s = atServo.setServo(1);
                blCheck.ifBool(timer.tRange(1500, 50));
                chassis.setStandard(-135);
            break; case 8: //move to mountain base
                s = chassis.straightMotors(-1,0.05,2);
                blCheck.ifBool(chassis.pRange(25,1.5));
            break; case 9: //move to mountain lowzone, touching first churro
                s = chassis.straightMotors(-0.50,0.05,2);
                blCheck.ifBool(chassis.pRange(37.5,1.5));
            break; case 10: //move to mountain midzone
                s = chassis.straightMotors(-0.30,0.03,1.5);
                blCheck.ifBool(chassis.pRange(0,32.5));
            break; default: //do nothing
            break;
        }
        //what the robot is doing (turn, straight, stop, servo...)
        telemetry.addData("Status",s);
    }
    @Override
    //shuts motors off
    public void stop() {
        chassis.stop();
        atServo.setServo(0.5);
        timer.resetTimer();
        while (timer.systemTime < 5000) {
        timer.update();
        telemetry.addData("Status","Stopped");
        }
    }
}