package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class LoganAuto extends LinearOpMode
{
    DcMotor lefty;
    DcMotor righty;

    int leftyEncoderOffset;
    int rightyEncoderOffset;

    @Override
    public void runOpMode() throws InterruptedException {
        lefty = hardwareMap.dcMotor.get("lefty");
        righty = hardwareMap.dcMotor.get("righty");
        lefty.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        leftyEncoderOffset = lefty.getCurrentPosition();
        rightyEncoderOffset = righty.getCurrentPosition();

        lefty.setPower(0.3);
        righty.setPower(0.3);

        while(lefty.getCurrentPosition() < 1440 * 5 + leftyEncoderOffset) {
            waitOneFullHardwareCycle();
        }

        lefty.setPower(0);
        righty.setPower(0);
    }
}
