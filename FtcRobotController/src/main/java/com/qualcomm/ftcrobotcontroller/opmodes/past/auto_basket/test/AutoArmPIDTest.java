package com.qualcomm.ftcrobotcontroller.opmodes.past.auto_basket.test;


import com.qualcomm.ftcrobotcontroller.opmodes.past.auto_basket.AutoBasketBase;
import com.qualcomm.ftcrobotcontroller.opmodes.past.auto_basket.components.AutoArmController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.control.PIDSettings;

public class AutoArmPIDTest extends AutoBasketBase
{
    AutoArmController armController;
    PIDSettings pidArmSettings;

    @Override
    public void init()
    {
        super.init();

        pidArmSettings = new PIDSettings(0,0,0);
        armController = new AutoArmController(motorArm, pidArmSettings, telemetry);
    }

    @Override
    public void start()
    {
        super.start();

        armController.start();
    }

    @Override
    public void loop()
    {
        drive();

        tunePID(pidArmSettings, gamepad2);


        telemetry.addData("Z: PID settings = {",
                pidArmSettings.getProportionalTerm() + ", "
                        + pidArmSettings.getIntegralTerm() + ", "
                        + pidArmSettings.getDerivativeTerm() + "}");

        //Move arm
        double targetAngle = armController.getTargetPositionDegrees() + (gamepad1.right_stick_y / -10);
        if (gamepad1.a)
        {
            targetAngle = 90;
        }
        armController.setTargetPositionDegrees(targetAngle);

        armController.updatePosition();
    }


    void drive()
    {
        //Manual driving
        double leftDrivePower = ((gamepad1.left_stick_y * -1) + gamepad1.left_stick_x)/3;
        double rightDrivePower = ((gamepad1.left_stick_y * -1) - gamepad1.left_stick_x)/3;

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
