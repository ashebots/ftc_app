//This is an autonomous program for driving to and pushing the correct button.

package com.qualcomm.ftcrobotcontroller.opmodes.past.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

public class TestButton extends LinearOpMode
{
    //define hardware
    DcMotor right;
    DcMotor left;
    Servo push;
    int rrotation = 0;
    int lrotation = 0;
    double leftpwr = 0.5;
    double rightpwr = 0.5;
    int firstdistance = 1000;

    @Override
    public void runOpMode() throws InterruptedException
    {
        //set up hardware
        left = hardwareMap.dcMotor.get("left");
        left.setDirection(DcMotor.Direction.REVERSE);
        left.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        right = hardwareMap.dcMotor.get("right");
        right.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        push = hardwareMap.servo.get("push");

        waitForStart();

        //run program
        while (rightpwr > 0 || leftpwr > 0)
        {
            left.setPower(leftpwr);
            right.setPower(rightpwr);
            lrotation = left.getCurrentPosition();
            if (lrotation > firstdistance)
            {
                leftpwr = 0;
            }
            rrotation = right.getCurrentPosition();
            if (rrotation > firstdistance)
            {
                rightpwr = 0;
            }
        }
        left.setPower(0.5);
        right.setPower(0.5);
        lrotation = left.getCurrentPosition();
        rrotation = right.getCurrentPosition();
        telemetry.addData("1: Time Elapsed", getRuntime());
        telemetry.addData("2: Left Motor", lrotation);
        telemetry.addData("3: Right Motor", rrotation);
    }
}
