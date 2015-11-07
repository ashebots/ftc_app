package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ashebots.ftcandroidlib.motor.MotorWheel;

public class AutoDistanceDrive extends LinearOpMode
{
    MotorWheel motorDriveLeft;
    MotorWheel motorDriveRight;

    @Override
    public void runOpMode() throws InterruptedException
    {
        motorDriveLeft = new MotorWheel(hardwareMap.dcMotor.get("motorDriveLeft"));
        motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRight = new MotorWheel(hardwareMap.dcMotor.get("motorDriveRight"));

        waitForStart();

        motorDriveLeft.setEncoderTicksPerRevolution(1440);
        motorDriveRight.setEncoderTicksPerRevolution(1440);

        motorDriveLeft.setDistancePerRevolution(4.0f);
        motorDriveRight.setDistancePerRevolution(4.0f);

        motorDriveLeft.runForDistance(12.0f, 0.25f);
        motorDriveRight.runForDistance(12.0f, 0.25f);


        for (int i = 0; i < 10; i++) {
            sleep(1000);
            telemetry.addData("1: left target = ", "Test: " + motorDriveLeft.calculateEncoderTicksForDistance(12.0f));
            telemetry.addData("2: right target = ", "Test: " + motorDriveRight.calculateEncoderTicksForDistance(12.0f));
            telemetry.addData("3: left encoder = ", motorDriveLeft.getCurrentPosition());
            telemetry.addData("4: right encoder = ", motorDriveRight.getCurrentPosition());
        }
    }

}
