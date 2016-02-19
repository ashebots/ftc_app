/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import com.qualcomm.ftcrobotcontroller.opmodes.Driving;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.ashebots.ftcandroidlib.motor.Motor;

public abstract class ClimbersToMountain extends Driving
{
    LegacyModule Legacy;
    UltrasonicSensor SonarSensorL;
    UltrasonicSensor SonarSensorR;
    DcMotor arm;
    Servo sarm;
    int ultrasonicPortL = 4;
    int ultrasonicPortR = 5;

    double neg;

    boolean startOnOtherSquare;

    boolean Mountain;

    double start2Turn = 24;
    double turn2Push = -45;
    double push2Wall = 20;
    double wall2Perp = 0;
    double perp2Basket = 6;
    double basket2Arm = -90;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        initBNO055();
        systemTimeSetup();

        Legacy = hardwareMap.legacyModule.get("legacy");
        Legacy.enable9v(ultrasonicPortL, true);
        Legacy.enable9v(ultrasonicPortR, true);
        SonarSensorL = hardwareMap.ultrasonicSensor.get("sonicL");
        SonarSensorR = hardwareMap.ultrasonicSensor.get("sonicR");
        arm = hardwareMap.dcMotor.get("armMotor");
        sarm = hardwareMap.servo.get("climberDumper");
        arm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        waitForStart();

        readBNO();

        if (startOnOtherSquare) {
            turnOnSpotPID(-90*neg,5,2.5,0.5,0.1,neg==1);
            moveForwardCorrection(24,1,0.25,0.1,5,0.25);
            turnOnSpotPID(0,5,2.5,0.5,0.1,neg==-1);
        }

        //move forward to clear mountain
        telemetry.addData("Clearing...", 0);
        moveForwardCorrection(start2Turn, 1, 0.5, 0.1, 5, 2.5);

        //move toward button
        telemetry.addData("Plowing...",0);
        turnOnSpotPID(turn2Push*neg,5,2.5,0.4,0.1,neg==1);
        moveForwardCorrectionBackInit(turn2Push*neg);
        if (neg==1) {
            while(SonarSensorL.getUltrasonicLevel()>push2Wall || SonarSensorL.getUltrasonicLevel()==0) {
                moveForwardCorrectionBackground(0,1,0.05,10,5);
                telemetry.addData("Sonic",SonarSensorL.getUltrasonicLevel());
            }
        } else {
            while(SonarSensorR.getUltrasonicLevel()>push2Wall || SonarSensorR.getUltrasonicLevel()==0) {
                moveForwardCorrectionBackground(0,1,0.05,10,5);
                telemetry.addData("Sonic",SonarSensorR.getUltrasonicLevel());
            }
        }

        if (Mountain) {
            moveForwardCorrection(-16,-1,0.25,0.1,5,2.5);
            turnOnSpotPID(45*neg,5,2.5,.375,.125,neg==-1);
            moveForwardCorrection(-50,-1,0.25,0.1,5,2.5);
        } else {
            //insert arm code here
            telemetry.addData("Dumping...",0);
            turnOnSpot(basket2Arm * neg, 2.5, 0.5, true);

            arm.setPower(1);
            Thread.sleep(100);
            arm.setPower(0);
            sarm.setPosition(1);
            arm.setPower(1);
            Thread.sleep(100);
            arm.setPower(0);
            sarm.setPosition(0);
        }
    }
}

