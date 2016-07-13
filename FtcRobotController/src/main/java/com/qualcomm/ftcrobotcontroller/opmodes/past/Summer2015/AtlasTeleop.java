//I know this is gross. We'll write pretty code when we have time.

package com.qualcomm.ftcrobotcontroller.opmodes.past.Summer2015;

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
        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorDriveRight.setDirection(DcMotor.Direction.REVERSE);

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

        telemetryUpdate();
    }

    private void telemetryUpdate()
    {
        String touchRingFrontString = (touchRingFront.isPressed()) ? "Front Ring: HEAVY" : "Front Ring: LIGHT";
        telemetry.addData("1", touchRingFrontString);

        String touchRingBackString = (touchRingBack.isPressed()) ? "Back Ring: HEAVY" : "Back Ring: LIGHT";
        telemetry.addData("2", touchRingBackString);

        //telemetry.addData("motorDriveLeftEncoder", "Left Drive Encoder: " + motorDriveLeft.getCurrentPosition());
        //telemetry.addData("motorDriveRightEncoder", "Right Drive Encoder: " + motorDriveRight.getCurrentPosition());
    }
}
