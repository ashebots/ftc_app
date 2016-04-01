package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.test;


import com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.AutoBasketBase;

import org.ashebots.ftcandroidlib.motor.Motor;

public class CalibrateDistance extends AutoBasketBase
{
    @Override
    public void start()
    {
        super.start();

        motorDriveLeft.setCurrentPosition(0);
        motorDriveRight.setCurrentPosition(0);
    }

    @Override
    public void loop()
    {
        rotateThreeTimes(motorDriveLeft);
        rotateThreeTimes(motorDriveRight);

        telemetry.addData("left encoder = ", motorDriveLeft.getCurrentPosition());
        telemetry.addData("right encoder = ", motorDriveRight.getCurrentPosition());
    }

    void rotateThreeTimes(Motor motor)
    {
        if (motor.getCurrentPosition() < motor.getEncoderTicksPerRevolution() * 3)
        {
            motor.setPower(0.3);
        }
        else
        {
            motor.setPower(0);
        }
    }
}
