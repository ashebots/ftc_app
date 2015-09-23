//Currently controls a 2 motor robot with differential drive

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TestDrive extends OpMode
{
    DcMotor motorDriveLeft;
    DcMotor motorDriveRight;

    public void init()
    {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");

        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorDriveRight.setDirection(DcMotor.Direction.REVERSE);
    }

    public void loop()
    {
        double drivePowerLeft = gamepad1.left_stick_y * -1;
        double drivePowerRight = gamepad1.right_stick_y * -1;
        if (gamepad1.right_bumper == false)
        {
            drivePowerLeft = drivePowerLeft * 0.5 / 1.0;
            drivePowerRight = drivePowerRight * 0.5 / 1.0;
        }

        motorDriveLeft.setPower(drivePowerLeft);
        motorDriveRight.setPower(drivePowerRight);
    }
}
