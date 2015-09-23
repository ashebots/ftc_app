//This is a real quick autonomous test for a robot with 2 wheels using differential drive

//Currently being created by Alex, and is not quite finished

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TestAuto extends LinearOpMode
{
    DcMotor motorRight;
    DcMotor motorLeft;
    @Override
    public void runOpMode() throws InterruptedException
    {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        motorRight = hardwareMap.dcMotor.get("motorRight");

        waitForStart();

        for (int counter = 0; counter < 4; counter++)
        {
            motorLeft.setPower(.5);
            motorRight.setPower(.5);
        }
    }
}
