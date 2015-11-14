
/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class TestAutoMountainRC extends LinearOpMode
{
    DcMotor motorRight;
    DcMotor motorLeft;
    double num1 = 10777;
    double num2 = -6400/3;
    double roffset;
    double loffset;
    @Override
    public void runOpMode() throws InterruptedException
    {
        motorLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        waitForStart();

        Thread.sleep(10000);

        motorLeft.setPower(.5);
        motorRight.setPower(.5);
        roffset = motorRight.getCurrentPosition();
        loffset = motorLeft.getCurrentPosition();
        while(motorLeft.getCurrentPosition()-loffset<num1) {
            telemetry.addData("Left Motor", motorLeft.getCurrentPosition()-loffset);
            telemetry.addData("Right Motor", motorRight.getCurrentPosition()-roffset);
            waitOneFullHardwareCycle();
        }

        roffset = motorRight.getCurrentPosition();
        loffset = motorLeft.getCurrentPosition();
        motorLeft.setPower(-.5);
        while(motorLeft.getCurrentPosition()-loffset>num2) {
            telemetry.addData("Left Motor", motorLeft.getCurrentPosition()-loffset);
            telemetry.addData("Right Motor", motorRight.getCurrentPosition()-roffset);
            waitOneFullHardwareCycle();
        }
        motorLeft.setPower(.5);
    }
}
