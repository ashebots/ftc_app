/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.art.Driving;

public abstract class TestAutoMountain extends Driving
{
    double floorDistance;
    double mountAngle;
    boolean leftMotorNeg;
    double mountDistance = 100000;

    @Override
    public void runOpMode() throws InterruptedException
    {
        initMotors();
        initBNO055();
        systemTimeSetup();

        waitForStart();

        readBNO();

        Thread.sleep(2000);
    }
}

