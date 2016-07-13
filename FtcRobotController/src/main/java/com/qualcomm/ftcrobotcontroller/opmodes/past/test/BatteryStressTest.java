package com.qualcomm.ftcrobotcontroller.opmodes.past.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class BatteryStressTest extends OpMode
{
    double minutesToRun = 15;

    DcMotor motor1;
    DcMotor motor2;

    VoltageSensor voltageSensor;

    @Override
    public void init()
    {
        motor1 = hardwareMap.dcMotor.get("motor1");
        motor2 = hardwareMap.dcMotor.get("motor2");

        voltageSensor = hardwareMap.voltageSensor.get("motors");
    }


    @Override
    public void loop()
    {
        if (getRuntime() / 60 > minutesToRun)
        {
            motor1.setPower(0);
            motor2.setPower(0);
            telemetry.addData("Test = ", "finished");
        }
        else
        {
            motor1.setPower(1);
            motor2.setPower(1);
        }

        telemetry.addData("RC voltage = ", voltageSensor.getVoltage());
        telemetry.addData("Minutes ran = ", getRuntime() / 60 + "/" + minutesToRun);
        telemetry.addData("Percent finished = ", (getRuntime() / 60) / minutesToRun * 100 + "%");
    }
}
