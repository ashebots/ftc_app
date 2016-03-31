package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.test;


import com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.AutoBasketBase;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.components.AutoDriveController;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.ashebots.ftcandroidlib.control.PIDSettings;

public class AutoFullDrivingTest extends AutoBasketBase
{
    AutoDriveController driveController;

    PIDSettings headingPIDSettings;
    PIDSettings distancePIDSettings;


    @Override
    public void init()
    {
        super.init();

        driveController = new AutoDriveController(motorDriveLeft, motorDriveRight, imu, headingPIDSettings, distancePIDSettings);
    }

    @Override
    public void start()
    {
        super.start();

        driveController.start();
    }

    @Override
    public void loop()
    {
        drive();

        tunePID(distancePIDSettings, gamepad2);

        telemetry.addData("Z: PID settings = {",
                distancePIDSettings.getProportionalTerm() + ", "
                        + distancePIDSettings.getIntegralTerm() + ", "
                        + distancePIDSettings.getDerivativeTerm() + "}");

    }

    boolean buttonPressed = false;
    boolean buttonPressedLast = false;
    void drive()
    {
        buttonPressed = false;

        //TURN LEFT
        if (gamepad1.dpad_left)
        {
            buttonPressed = true;
            if (buttonPressedLast == false)
            {
                driveController.setHeadingRelative(-45);
            }
        }

        //TURN RIGHT
        if (gamepad1.dpad_right)
        {
            buttonPressed = true;
            if (buttonPressedLast == false)
            {
                driveController.setHeadingRelative(45);
            }
        }

        //DRIVE 6 INCHES
        if (gamepad1.a)
        {
            buttonPressed = true;
            if (buttonPressedLast == false)
            {
                driveController.setDriveDistanceRelative(6);
            }
        }

        //DRIVE 12 INCHES
        if (gamepad1.b)
        {
            buttonPressed = true;
            if (buttonPressedLast == false)
            {
                driveController.setDriveDistanceRelative(12);
            }
        }

        //DRIVE 36 INCHES
        if (gamepad1.y)
        {
            buttonPressed = true;
            if (buttonPressedLast == false)
            {
                driveController.setDriveDistanceRelative(36);
            }
        }

        /*
        if (gamepad1.)
        {
            buttonPressed = true;
            if (buttonPressedLast == false)
            {

            }
        }
         */

        buttonPressedLast = buttonPressed;


        driveController.updatePosition();
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
