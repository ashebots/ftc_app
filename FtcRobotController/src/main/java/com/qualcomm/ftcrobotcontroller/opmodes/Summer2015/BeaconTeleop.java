//I know this is gross. We'll write pretty code when we have time.

package com.qualcomm.ftcrobotcontroller.opmodes.Summer2015;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;

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

    LightSensor forceRing;

    ChassisOmni chassis;

    @Override
    public void init()
    {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorDriveRight.setDirection(DcMotor.Direction.REVERSE);
        motorDriveFront = hardwareMap.dcMotor.get("motorDriveFront");
        motorDriveBack = hardwareMap.dcMotor.get("motorDriveBack");
        motorDriveBack.setDirection(DcMotor.Direction.REVERSE);

        forceRing = hardwareMap.lightSensor.get("forceRing");

        chassis = new ChassisOmni(motorDriveLeft, motorDriveRight, motorDriveFront, motorDriveBack);
    }

    @Override
    public void loop()
    {
        try
        {
            double leftDrivePower = chassis.Drive(gamepad1.left_stick_x, gamepad1.left_stick_y * -1.0f, gamepad1.right_stick_x);
            telemetry.addData("4", "leftDrivePower: " + leftDrivePower);
            telemetryUpdate();
        }
        catch (Exception exception)
        {
            telemetry.addData("3", "Error: " + exception.getMessage());
        }
    }

    private void telemetryUpdate()
    {
        telemetry.addData("1", "Ring weight raw value: " + forceRing.getLightDetectedRaw());
        telemetry.addData("2", "Ring weight processed value: " + forceRing.getLightDetected());
    }
}
