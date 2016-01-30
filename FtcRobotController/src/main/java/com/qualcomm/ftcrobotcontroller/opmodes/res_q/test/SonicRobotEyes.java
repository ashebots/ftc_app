/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class SonicRobotEyes extends LinearOpMode
{
    LegacyModule Legacy;
    UltrasonicSensor SonarSensor;
    int ultrasonicPort = 5;

    @Override
    public void runOpMode() throws InterruptedException {
        Legacy = hardwareMap.legacyModule.get("legacy");
        Legacy.enable9v(ultrasonicPort, true);
        SonarSensor = hardwareMap.ultrasonicSensor.get("sonicR");

        waitForStart();

        while (true) {
            telemetry.addData("SonicLevel",SonarSensor.getUltrasonicLevel());
        }
    }
}

