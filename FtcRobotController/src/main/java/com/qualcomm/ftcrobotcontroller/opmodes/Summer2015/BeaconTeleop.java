package com.qualcomm.ftcrobotcontroller.opmodes.Summer2015;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ashebots.ftcandroidlib.drive.ChassisOmni;

/**
 * Beacon is our omni-drive, with the weird lifty thing
 */
public class BeaconTeleop extends OpMode
{
    DcMotor motorDriveLeft;
    DcMotor motorDriveRight;
    DcMotor motorDriveFront;
    DcMotor motorDriveBack;

    ChassisOmni chassis;

    @Override
    public void init()
    {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorDriveFront = hardwareMap.dcMotor.get("motorDriveFront");
        motorDriveFront.setDirection(DcMotor.Direction.REVERSE);
        motorDriveBack = hardwareMap.dcMotor.get("motorDriveBack");

        chassis = new ChassisOmni(motorDriveLeft, motorDriveRight, motorDriveFront, motorDriveBack);
    }

    @Override
    public void loop()
    {
        chassis.Drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
    }
}
