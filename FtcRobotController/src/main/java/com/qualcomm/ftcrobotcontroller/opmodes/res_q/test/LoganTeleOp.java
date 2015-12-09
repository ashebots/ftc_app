package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class LoganTeleOp extends OpMode
{
    DcMotor lefty;
    DcMotor righty;

    @Override
    public void init() {
        lefty = hardwareMap.dcMotor.get("lefty");
        righty = hardwareMap.dcMotor.get("righty");
        lefty.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        lefty.setPower(gamepad1.left_stick_y * -1);
        righty.setPower(gamepad1.right_stick_y * -1);
    }
}
