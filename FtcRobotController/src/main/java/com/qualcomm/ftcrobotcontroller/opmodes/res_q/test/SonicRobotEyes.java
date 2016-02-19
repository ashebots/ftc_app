/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class SonicRobotEyes extends LinearOpMode
{
    LegacyModule Legacy;
    UltrasonicSensor SonarSensor;
    int ultrasonicPort = 5;

    Servo PlowL;
    Servo PlowR;

    @Override
    public void runOpMode() throws InterruptedException {
        Legacy = hardwareMap.legacyModule.get("legacy");
        Legacy.enable9v(ultrasonicPort, true);
        SonarSensor = hardwareMap.ultrasonicSensor.get("sonicR");

        PlowL = hardwareMap.servo.get("plowL");
        PlowR = hardwareMap.servo.get("plowR");

        waitForStart();

        while (true) {
            telemetry.addData("SonicLevel",SonarSensor.getUltrasonicLevel());

            PlowL.setPosition(1 - SonarSensor.getUltrasonicLevel() / 255);
            PlowR.setPosition(SonarSensor.getUltrasonicLevel() / 255);
        }
    }
}

