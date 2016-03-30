package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.test;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.AutoBasketBase;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.control.PIDController;
import org.ashebots.ftcandroidlib.control.PIDSettings;

public class IMUDrivePID extends AutoBasketBase
{
    PIDController pidDriveController;
    PIDSettings pidDriveSettings;

    double targetYaw = 0; //continuous yaw

    @Override
    public void init()
    {
        super.init();

        pidDriveSettings = new PIDSettings(0.0002,0,0);
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

        tunePID(pidDriveSettings, gamepad2);
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

            leftDrivePower -= angleError;
            rightDrivePower += angleError;
        }

        //Manual driving
        leftDrivePower += ((gamepad1.left_stick_y * -1) + gamepad1.left_stick_x)/3;
        rightDrivePower += ((gamepad1.left_stick_y * -1) - gamepad1.left_stick_x)/3;

        leftDrivePower = Range.clip(leftDrivePower, -1, 1);
        rightDrivePower = Range.clip(rightDrivePower, -1, 1);

        motorDriveLeft.setPower(leftDrivePower);
        motorDriveRight.setPower(rightDrivePower);
    }


    void tunePID(PIDSettings settings, Gamepad gamepad)
    {

    }
}
