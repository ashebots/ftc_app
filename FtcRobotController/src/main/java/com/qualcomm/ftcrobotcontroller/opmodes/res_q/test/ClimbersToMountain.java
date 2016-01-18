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

    double clearDistance = 13.5;
    double buttAngle;
    double buttDistance = 65.5;
    double mountDistance = 18;
    double mountAngle;
    double chargeDistance = 50;
    double neg;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        initArm();
        initBNO055();
        systemTimeSetup();

        waitForStart();

        readBNO();

        Thread.sleep(0);

        readBNO();

        //move forward to clear mountain
        moveForwardCorrection(clearDistance, 1, 0.25, 0.01, 5, 2.5);

        //turn towards button
        turnOnSpotPID(buttAngle, 5, 2.5, 0.25, 0.025, leftMotorNeg);

        //move toward button
        moveForwardCorrection(buttDistance, 1, 0.25, 0.025, 5, 2.5);

        //insert arm code here
        turnTable(600*neg,0.025*neg);
        moveArmInit();
        while(!armFinish) {
            moveArm(8500 ,0.25);
        }
        Thread.sleep(2000);
        stopArm();
        turnTable(-600*neg,-0.025*neg);
        moveArmInit();
        while(!armFinish) {
            moveArm(8500, -0.25);
        }
        stopArm();
    }
}

