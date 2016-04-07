package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.test;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.AutoBasketBase;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.control.PIDController;
import org.ashebots.ftcandroidlib.control.PIDSettings;

public class IMUDriveRotatePID extends AutoBasketBase
{
    PIDController pidDriveController;
    PIDSettings pidDriveSettings;

    double targetYaw = 0; //continuous yaw

    @Override
    public void init()
    {
        super.init();

        pidDriveSettings = new PIDSettings(0.055, 0, 0.002);
        pidDriveController = new PIDController(pidDriveSettings);
    }


    @Override
    public void loop()
    {
        imu.updateAngles();
        telemetry.addData("roll = ", imu.getRoll());
        telemetry.addData("pitch = ", imu.getPitch());
        telemetry.addData("yaw = ", imu.getYaw());
        telemetry.addData("continuousYaw = ", imu.getContinuousYaw());

        drive();
        telemetry.addData("targetYaw = ", targetYaw);

        tunePID(pidDriveSettings, gamepad2);

        telemetry.addData("Z: PID settings = {",
                pidDriveSettings.getProportionalTerm() + ", "
                        + pidDriveSettings.getIntegralTerm() + ", "
                        + pidDriveSettings.getDerivativeTerm() + "}");


        //PLOW
        double plowPower = 0.5;
        if (gamepad1.left_bumper)
        {
            plowPower += 0.2;
        }
        if (gamepad1.left_trigger > 0.5)
        {
            plowPower -= 0.2;
        }
        servoPlow.setPosition(plowPower);
    }


    void drive()
    {
        double leftDrivePower = 0;
        double rightDrivePower = 0;

        //Create setpoint for yaw
        if (gamepad1.a)
        {
            targetYaw = imu.getContinuousYaw();
        }

        //Calculate PID and move to target
        if (gamepad1.right_bumper)
        {
            double angleError = pidDriveController.calculate(imu.getContinuousYaw(), targetYaw);

            if (Double.isNaN(angleError))
            {
                telemetry.addData("angleError = ", "NaN");
            }
            else
            {
                telemetry.addData("angleError = ", angleError);
                leftDrivePower += angleError;
                rightDrivePower -= angleError;
            }
        }

        //Manual driving
        leftDrivePower += ((gamepad1.left_stick_y * -1) + gamepad1.left_stick_x)/3;
        rightDrivePower += ((gamepad1.left_stick_y * -1) - gamepad1.left_stick_x)/3;

        leftDrivePower = Range.clip(leftDrivePower, -1.0, 1.0);
        rightDrivePower = Range.clip(rightDrivePower, -1.0, 1.0);

        motorDriveLeft.setPower(leftDrivePower);
        motorDriveRight.setPower(rightDrivePower);
    }


    void tunePID(PIDSettings settings, Gamepad gamepad)
    {
        //Configure P
        if (gamepad.dpad_up)
            settings.setProportionalTerm(settings.getProportionalTerm() + 0.00001);
        if (gamepad.dpad_down)
            settings.setProportionalTerm(settings.getProportionalTerm() - 0.00001);

        //Configure I
        if (gamepad.left_bumper)
            settings.setIntegralTerm(settings.getIntegralTerm() + 0.00001);
        if (gamepad.left_trigger > .2)
            settings.setIntegralTerm(settings.getIntegralTerm() - 0.00001);

        //Configure D
        if (gamepad.right_bumper)
            settings.setDerivativeTerm(settings.getDerivativeTerm() + 0.00001);
        if (gamepad.right_trigger > .2)
            settings.setDerivativeTerm(settings.getDerivativeTerm() - 0.00001);
    }
}
