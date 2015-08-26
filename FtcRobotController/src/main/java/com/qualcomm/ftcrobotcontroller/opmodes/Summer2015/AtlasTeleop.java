package com.qualcomm.ftcrobotcontroller.opmodes.Summer2015;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ashebots.ftcandroidlib.drive.ChassisArcade;

/**
 * Atlas is our robot with the linear slide lift
 */
public class AtlasTeleop extends OpMode
{
    DcMotor motorDriveLeft;
    DcMotor motorDriveRight;
    DcMotor motorLinearSlideLift;

    ChassisArcade chassis;

    @Override
    public void init() {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");

        motorLinearSlideLift = hardwareMap.dcMotor.get("motorLinearSlideLift");


        chassis = new ChassisArcade(motorDriveLeft, motorDriveRight);
    }

    @Override
    public void loop() {
        //Drive
        chassis.Drive(gamepad1.left_stick_x, gamepad1.left_stick_y);

        motorLinearSlideLift.setPower(gamepad1.right_stick_y);

        telemetry.addData("motorDriveLeftEncoder", "Left Drive Encoder: " + motorDriveLeft.getCurrentPosition());
        telemetry.addData("motorDriveRightEncoder", "Right Drive Encoder: " + motorDriveRight.getCurrentPosition());

    }
}
