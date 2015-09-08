//I know this is gross. We'll write pretty code when we have time.

package com.qualcomm.ftcrobotcontroller.opmodes.Summer2015;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.ashebots.ftcandroidlib.drive.ChassisArcade;

/**
 * Atlas is our robot with the linear slide lift
 */
public class AtlasTeleop extends OpMode
{
    DcMotor motorDriveLeft;
    DcMotor motorDriveRight;
    DcMotor motorLinearSlideLift;

    TouchSensor touchRingFront;
    TouchSensor touchRingBack;

    ChassisArcade chassis;

    @Override
    public void init() {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");

        motorLinearSlideLift = hardwareMap.dcMotor.get("motorLinearSlideLift");

        touchRingFront = hardwareMap.touchSensor.get("touchRingFront");
        touchRingBack = hardwareMap.touchSensor.get("touchRingBack");

        chassis = new ChassisArcade(motorDriveLeft, motorDriveRight);
    }

    @Override
    public void loop() {
        //Drive
        chassis.Drive(gamepad1.left_stick_x, gamepad1.left_stick_y * -1); //Y is inverted here b/c the joystick gives up as a negative, but the Drive method wants up as a positive

        motorLinearSlideLift.setPower(gamepad1.right_stick_y);


        telemetry.addData("touchRingFrontPressed", "Front");

        telemetry.addData("motorDriveLeftEncoder", "Left Drive Encoder: " + motorDriveLeft.getCurrentPosition());
        telemetry.addData("motorDriveRightEncoder", "Right Drive Encoder: " + motorDriveRight.getCurrentPosition());
    }

    private void telemetryUpdate()
    {
        String touchRingFrontString = (touchRingFront.isPressed()) ? "Front Ring: HEAVY" : "Front Ring: LIGHT";
        telemetry.addData("touchRingFrontPressed", touchRingFrontString);

        String touchRingBackString = (touchRingBack.isPressed()) ? "Back Ring: HEAVY" : "Back Ring: LIGHT";
        telemetry.addData("touchRingBackPressed", touchRingBackString);


        telemetry.addData("motorDriveLeftEncoder", "Left Drive Encoder: " + motorDriveLeft.getCurrentPosition());
        telemetry.addData("motorDriveRightEncoder", "Right Drive Encoder: " + motorDriveRight.getCurrentPosition());
    }
}
