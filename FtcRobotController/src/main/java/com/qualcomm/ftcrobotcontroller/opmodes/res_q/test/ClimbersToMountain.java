/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import com.qualcomm.ftcrobotcontroller.opmodes.Driving;

public abstract class ClimbersToMountain extends Driving
{
    boolean leftMotorNeg;

    double clearDistance = 5;
    double buttAngle;
    double buttDistance = 70;
    double reverseAngle;
    double mountDistance = 50;
    double mountAngle;
    double chargeDistance = 1000;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        initBNO055();
        systemTimeSetup();

        waitForStart();

        readBNO();

        Thread.sleep(0);

        readBNO();

        //move forward to clear mountain
        moveForwardCorrection(clearDistance, 1, 0.5, 0.01, 5, 2.5);

        //turn towards button
        turnOnSpotPID(buttAngle, 5, 2.5, 0.5, 0.025, leftMotorNeg);

        //move toward button
        moveForwardCorrection(buttDistance, 1, 0.5, 0.025, 5, 2.5);

        //insert arm code here

        //turn 180
        turnOnSpotPID(reverseAngle, 5, 2.5, 0.5, 0.025, leftMotorNeg);

        //move to mountain
        moveForwardCorrection(mountDistance, 1, 0.5, 0.025, 5, 2.5);

        //turn towards mountain
        turnOnSpotPID(mountAngle, 5, 2.5, 0.5, 0.025, !(leftMotorNeg));

        //drive up mountain
        moveForwardCorrection(chargeDistance, 1, 0.5, 0.025, 5, 2.5);
    }
}

