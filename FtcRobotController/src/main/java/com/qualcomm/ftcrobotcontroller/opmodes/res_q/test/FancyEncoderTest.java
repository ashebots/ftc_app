package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ashebots.ftcandroidlib.motor.Motor;

public class FancyEncoderTest extends OpMode
{
    DcMotor dcMotor;
    Motor motor;

    public void init()
    {
        dcMotor = hardwareMap.dcMotor.get("testMotor");
        motor = new Motor(dcMotor);
    }

    public void loop()
    {
        if (gamepad1.dpad_up)
            motor.setCurrentPosition(10000);

        if (gamepad1.dpad_down)
            motor.setCurrentPosition(-10000);

        if (gamepad1.dpad_left)
            motor.setCurrentPosition(0);


        motor.setPower(gamepad1.right_stick_y * -1);

        telemetry.addData("1: currentPosition = ", motor.getCurrentPosition());
        telemetry.addData("2: currentPositionRaw = ", motor.getCurrentPositionRaw());
        telemetry.addData("3: currentPositionOffset = ", motor.getCurrentPositionOffset());
    }
}
