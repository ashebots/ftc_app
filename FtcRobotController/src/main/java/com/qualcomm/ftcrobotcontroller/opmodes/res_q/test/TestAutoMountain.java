
/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class TestAutoMountain extends LinearOpMode
{
    DcMotor motorRight;
    DcMotor motorLeft;
    double num1 = 1000;
    double num2 = 500;
    @Override
    public void runOpMode() throws InterruptedException
    {
        Thread.sleep(10000);
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        waitForStart();

        for (int counter = 0; counter < 4; counter++)
        {
            motorLeft.setPower(.5);
            motorRight.setPower(.5);
            while(motorLeft.getCurrentPosition()<num1) {
                waitOneFullHardwareCycle();
            }
            motorRight.setPower(-.5);
            while(motorLeft.getCurrentPosition()<num1+num2) {
                waitOneFullHardwareCycle();
            }
            motorRight.setPower(.5);
        }
    }
}
