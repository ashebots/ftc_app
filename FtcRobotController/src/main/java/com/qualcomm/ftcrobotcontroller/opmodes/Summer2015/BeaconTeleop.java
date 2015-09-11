//I know this is gross. We'll write pretty code when we have time.

package com.qualcomm.ftcrobotcontroller.opmodes.Summer2015;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

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

    DcMotor motorArm;
    Servo servoWrist;
    LightSensor forceRing;


    @Override
    public void init()
    {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorDriveFront = hardwareMap.dcMotor.get("motorDriveFront");
        motorDriveBack = hardwareMap.dcMotor.get("motorDriveBack");

        chassis = new ChassisOmni(motorDriveLeft, motorDriveRight, motorDriveFront, motorDriveBack);

        motorArm = hardwareMap.dcMotor.get("motorArm");
        servoWrist = hardwareMap.servo.get("servoWrist");
        forceRing = hardwareMap.lightSensor.get("forceRing");
    }

    @Override
    public void loop()
    {
        try
        {
            //DRIVE
            double leftDrivePower = chassis.Drive(gamepad1.left_stick_x, gamepad1.left_stick_y * -1.0f, gamepad1.right_stick_x);

            //ARM
            float armPower = gamepad1.left_trigger / 10;
            if (gamepad1.left_bumper == true) //The bumper inverts the direction of the trigger
            {
                armPower *= -1;
            }
            motorArm.setPower(armPower);

            //WRIST
            float wristChange = gamepad1.right_trigger * 0.01f; //Made up :)
            if (gamepad1.right_bumper == false)
            {
                wristChange *= -1;
            }
            float wristPosNew = (float) Range.clip(servoWrist.getPosition() + wristChange, 0.0f, 1.0f);
            servoWrist.setPosition(wristPosNew);

            //TELEMETRY
            telemetry.addData("4", "wristChange: " + wristChange);
            telemetry.addData("5", "motorArmEncoder: " + motorArm.getCurrentPosition());
            //telemetry.addData("4", "leftDrivePower: " + leftDrivePower);
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