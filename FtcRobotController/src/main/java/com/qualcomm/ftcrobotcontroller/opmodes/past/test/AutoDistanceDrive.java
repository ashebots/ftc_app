package com.qualcomm.ftcrobotcontroller.opmodes.past.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

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

        motorDriveLeft.setDistancePerRevolution(12.56f);
        motorDriveRight.setDistancePerRevolution(12.56f);

        //motorDriveLeft.runForDistance(12.0f, 0.25f);
        //motorDriveRight.runForDistance(12.0f, 0.25f);
        driveForDistance(24f, 0.25f);


        for (int i = 0; i < 10; i++) {
            sleep(1000);
            telemetry.addData("1: left target = ", "Test: " + motorDriveLeft.calculateEncoderTicksForDistance(12.0f));
            telemetry.addData("2: right target = ", "Test: " + motorDriveRight.calculateEncoderTicksForDistance(12.0f));
            telemetry.addData("3: left encoder = ", motorDriveLeft.getCurrentPosition());
            telemetry.addData("4: right encoder = ", motorDriveRight.getCurrentPosition());
        }
    }


    void driveForDistance(float distance, float power) throws InterruptedException
    {
        motorDriveLeft.runForDistance(distance, power);
        motorDriveRight.runForDistance(distance, power);
        while (motorDriveLeft.isBusy() || motorDriveRight.isBusy())
        {
            sleep(10);
        }
        motorDriveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorDriveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }
}
