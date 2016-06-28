/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.art.Driving;

public abstract class TestAutoMountainCopy extends Driving
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

        Thread.sleep(0);

        //move forward in front of mountain
        moveForwardCorrection(floorDistance, 1, 0.5, 0.01, 5, 2.5);

        readBNO();

        //turn towards mountain
        turnOnSpotPID(mountAngle, 5, 2.5, 0.5, 0.025, leftMotorNeg);

        // charge up mountain
        while(true) {
            moveForwardCorrection(mountDistance, 1, 0.5, 0.025, 5, 2.5);
        }
    }
}

