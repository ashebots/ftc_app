//Currently controls a 2 motor robot with differential drive

package com.qualcomm.ftcrobotcontroller.opmodes.past.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class TestDrive4 extends OpMode
{
    DcMotor motorDriveFrontLeft;
    DcMotor motorDriveFrontRight;
    DcMotor motorDriveBackLeft;
    DcMotor motorDriveBackRight;

    public void init()
    {
        motorDriveFrontLeft = hardwareMap.dcMotor.get("motorDriveFrontLeft");
        motorDriveFrontLeft.setDirection(DcMotor.Direction.REVERSE);

        motorDriveFrontRight = hardwareMap.dcMotor.get("motorDriveFrontRight");


        motorDriveBackLeft = hardwareMap.dcMotor.get("motorDriveBackLeft");
        motorDriveBackLeft.setDirection(DcMotor.Direction.REVERSE);

        motorDriveBackRight = hardwareMap.dcMotor.get("motorDriveBackRight");
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

        motorDriveFrontLeft.setPower(drivePowerLeft);
        motorDriveFrontRight.setPower(drivePowerRight);
        motorDriveBackLeft.setPower(drivePowerLeft);
        motorDriveBackRight.setPower(drivePowerRight);
    }
}
