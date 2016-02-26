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

public class MountainSimple extends Driving
{
    LegacyModule Legacy;
    UltrasonicSensor SonarSensor;
    DcMotor arm;
    Servo sarm;
    int ultrasonicPort = 4;

    boolean Mountain;

    int neg;

    double start2Turn = 12;
    double push2Wall = 200;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        initBNO055();
        systemTimeSetup();

        Legacy = hardwareMap.legacyModule.get("legacy");
        Legacy.enable9v(ultrasonicPort, true);
        SonarSensor = hardwareMap.ultrasonicSensor.get("sonic");
        arm = hardwareMap.dcMotor.get("armMotor");
        sarm = hardwareMap.servo.get("climberDumper");
        arm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        waitForStart();

        //move forward to clear mountain
        telemetry.addData("Clearing...", 0);
        moveForwardCorrection(start2Turn, 1, 0.5, 0.1, 5, 2.5);

        //move toward button
        telemetry.addData("Plowing...", 0);
        moveForwardCorrectionBackInit(0);
        while(SonarSensor.getUltrasonicLevel()<push2Wall || SonarSensor.getUltrasonicLevel()==0 || SonarSensor.getUltrasonicLevel()==255) {
            moveForwardCorrectionBackground(0,1,0.05,10,5);
            telemetry.addData("Sonic",SonarSensor.getUltrasonicLevel());
            waitOneFullHardwareCycle();
        }
        turnOnSpotPID(45*neg,5,2.5,.375,.125,neg==-1);
        //insert mount code here
        moveForwardCorrectionBackInit(45*neg);
        while(retrieveBNOData('p') < 25) {
            moveForwardCorrectionBackground(1,0.5,0.1,5,10);
        }
        moveForwardCorrectionBackInit(45*neg);
        while(retrieveBNOData('p') < 45) {
            moveForwardCorrectionBackground(1,0.35,0.1,5,10);
        }
        moveForwardCorrectionBackInit(45*neg);
        while(!forwardFinish) {
            moveForwardCorrectionBackground(4, 0.25, 0.1, 5, 10);
        }
    }
}
