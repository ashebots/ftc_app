package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;


public class TelemetryTest extends OpMode {

    int i = 0;

    public TelemetryTest() {

    }

    public void start() {

    }

    public void loop() {
        telemetry.addData("i", "Number = " + i);
        i++;
    }

    public void stop() {

    }
}
