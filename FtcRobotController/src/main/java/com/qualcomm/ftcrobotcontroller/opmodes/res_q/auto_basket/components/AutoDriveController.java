package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.components;


import com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib.IMU;

import org.ashebots.ftcandroidlib.control.PIDController;
import org.ashebots.ftcandroidlib.control.PIDSettings;
import org.ashebots.ftcandroidlib.motor.Motor;

public class AutoDriveController
{
    IMU imu;

    Motor motorDriveLeft;
    Motor motorDriveRight;

    PIDController headingPIDController;
    PIDController distancePIDController;


    public AutoDriveController(Motor motorDriveLeft, Motor motorDriveRight, IMU imu, PIDSettings headingPIDSettings, PIDSettings distancePIDSettings)
    {
        this.motorDriveLeft = motorDriveLeft;
        this.motorDriveRight = motorDriveRight;

        this.imu = imu;

        this.headingPIDController = new PIDController(headingPIDSettings);
        this.distancePIDController = new PIDController(distancePIDSettings);
    }


    public void setHeadingRelative()
    {

    }


    //Inches; positive forward, negative back
    public void setDriveDistanceRelative()
    {

    }


    //MUST BE CALLED IN LOOP()
    public void updatePosition()
    {

    }
}
