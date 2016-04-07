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

public abstract class MountainSimple extends Driving
{
    //LegacyModule Legacy;
    //UltrasonicSensor SonarSensor;
    //int ultrasonicPort = 4;

    public Servo servoClimberDumper;

    public Servo servoLeverHitterLeft; //Refers to left "drive side"
    public Servo servoLeverHitterRight; //Refers to right "drive side"

    public Servo servoAllClearLeft;
    public Servo servoAllClearRight;

    int neg;

    //double push2Wall = 200;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        initBNO055();
        systemTimeSetup();

        servoClimberDumper = hardwareMap.servo.get("climberDumper"); //continuous servo

        servoLeverHitterLeft = hardwareMap.servo.get("leverHitterL");
        servoLeverHitterLeft.setDirection(Servo.Direction.REVERSE); //Should be that 0 is down //Unsure which should be reversed
        servoLeverHitterRight = hardwareMap.servo.get("leverHitterR");

        servoAllClearLeft = hardwareMap.servo.get("allClearL");
        servoAllClearLeft.setDirection(Servo.Direction.REVERSE);
        servoAllClearRight = hardwareMap.servo.get("allClearR");

        //Legacy = hardwareMap.legacyModule.get("legacy");
        //Legacy.enable9v(ultrasonicPort, true);
        //SonarSensor = hardwareMap.ultrasonicSensor.get("sonic");

        waitForStart();

        servoLeverHitterLeft.setPosition(0.99);
        servoLeverHitterRight.setPosition(0.99);

        servoAllClearLeft.setPosition(0.5);
        servoAllClearRight.setPosition(0.5);

        servoClimberDumper.setPosition(0.5);

        //move toward button
        telemetry.addData("Plowing...", 0);
        moveForwardCorrection(26,1,0.2,0.1,7.5,5);
        /*moveForwardCorrectionBackInit(0);
        while(SonarSensor.getUltrasonicLevel()<push2Wall || SonarSensor.getUltrasonicLevel()==0 || SonarSensor.getUltrasonicLevel()==255) {
            moveForwardCorrectionBackground(0,1,0.05,10,5);
            telemetry.addData("Sonic",SonarSensor.getUltrasonicLevel());
            waitOneFullHardwareCycle();
        }*/
        //insert mount code here
        turnOnSpotPID(45 * neg, 10, 5, 0.5, 0.25, neg==-1);
        moveForwardCorrectionBackInit(45 * neg);
        telemetry.addData("On: STARTING     BNO", retrieveBNOData('p'));
        while (Math.abs(retrieveBNOData('p')) < 25) {
            moveForwardCorrectionBackground(1,-0.5,0.1,2.5,1);
            telemetry.addData("On: Floor        BNO", retrieveBNOData('p'));
            if(motorPaused()) {
                while (true) {changeMotorSpeed(0);}
            }
        }
        moveForwardCorrectionBackInit(45 * neg);
        while(Math.abs(retrieveBNOData('p')) < 35) {
            moveForwardCorrectionBackground(1,-0.35,0.1,2.5,1);
            telemetry.addData("On: Low Zone     BNO", retrieveBNOData('p'));
        }
        moveForwardCorrectionBackInit(45 * neg);
        while(Math.abs(retrieveBNOData('p')) > 33) {
            moveForwardCorrectionBackground(1,-0.25,0.1,2.5,1);
            telemetry.addData("On: Mid Zone BNO", retrieveBNOData('p'));
        }
        telemetry.addData("On: STOPPED      BNO", retrieveBNOData('p'));
        changeMotorSpeed(0);
    }
}
