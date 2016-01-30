/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import com.qualcomm.ftcrobotcontroller.opmodes.Driving;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public abstract class ClimbersToMountain extends Driving
{
    LegacyModule Legacy;
    UltrasonicSensor SonarSensorL;
    UltrasonicSensor SonarSensorR;
    Servo PlowL;
    Servo PlowR;
    int ultrasonicPortL = 4;
    int ultrasonicPortR = 5;

    double neg;

    double servoPosition = 0;

    double start2Turn = 12;
    double turn2Push = -45;
    double push2Wall = 20;
    double wall2Perp = 0;
    double perp2Basket = 6;
    double basket2Arm = 90;

    double armDistance = 7000;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        initArm();
        initBNO055();
        systemTimeSetup();

        Legacy = hardwareMap.legacyModule.get("legacy");
        Legacy.enable9v(ultrasonicPortL, true);
        Legacy.enable9v(ultrasonicPortR, true);
        SonarSensorL = hardwareMap.ultrasonicSensor.get("sonicL");
        SonarSensorR = hardwareMap.ultrasonicSensor.get("sonicR");
        PlowL = hardwareMap.servo.get("plowL");
        PlowR = hardwareMap.servo.get("plowR");

        waitForStart();

        readBNO();

        Thread.sleep(0);

        readBNO();

        PlowL.setPosition(servoPosition);
        PlowR.setPosition(1-servoPosition);


        //move forward to clear mountain
        telemetry.addData("Clearing...",0);
        moveForwardCorrection(start2Turn, 1, 0.25, 0.01, 5, 2.5);

        //move toward button
        telemetry.addData("Plowing...",0);
        moveForwardCorrectionBackInit(turn2Push*neg);
        if (neg==1) {
            while(SonarSensorL.getUltrasonicLevel()>push2Wall || SonarSensorR.getUltrasonicLevel()==0) {
                moveForwardCorrectionBackground(0,1,0.025,10,5);
                telemetry.addData("Sonic",SonarSensorR.getUltrasonicLevel());
            }
        } else {
            while(SonarSensorR.getUltrasonicLevel()>push2Wall || SonarSensorL.getUltrasonicLevel()==0) {
                moveForwardCorrectionBackground(0,1,0.025,10,5);
                telemetry.addData("Sonic",SonarSensorL.getUltrasonicLevel());
            }
        }
        telemetry.addData("Finishing...",0);
        turnOnSpotPID(wall2Perp * neg, 5, 2.5, 0.25, 0.05, neg == 0);
        moveForwardCorrection(perp2Basket, 0.75, 0.25, 0.05, 5, 2.5);

        //insert arm code here
        telemetry.addData("Dumping...",0);
        turnOnSpot(basket2Arm * neg, 2.5, 0.5, true);
        //armServo.setPosition(0.6);

    }
}

