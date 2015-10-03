//Currently controls a 2 motor robot with differential drive

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class TestDrive extends OpMode
{
    DcMotor motorDriveLeft;
    DcMotor motorDriveRight;

    public void init()
    {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);

        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");
    }

    public void loop()
    {
        double drivePowerLeft = (gamepad1.left_stick_y * -1 ) + gamepad1.left_stick_x;
        double drivePowerRight = (gamepad1.left_stick_y * -1 ) - gamepad1.left_stick_x;
        if (gamepad1.right_bumper == false)
        {
            drivePowerLeft = drivePowerLeft * 0.5 / 1.0;
            drivePowerRight = drivePowerRight * 0.5 / 1.0;
        }
        Range.clip(drivePowerLeft, -1.0, 1.0);
        Range.clip(drivePowerRight, -1.0, 1.0);

        motorDriveLeft.setPower(drivePowerLeft);
        motorDriveRight.setPower(drivePowerRight);
    }
}
