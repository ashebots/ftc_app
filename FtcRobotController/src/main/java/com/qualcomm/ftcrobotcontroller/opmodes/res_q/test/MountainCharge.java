/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.art.Driving;

public class MountainCharge extends Driving
{
    @Override
    public void runOpMode() throws InterruptedException {
        initBNO055();
        systemTimeSetup();
        telemetry.addData("On: INITIATING   BNO", retrieveBNOData('p'));

        waitForStart();
        telemetry.addData("On: STARTING     BNO", retrieveBNOData('p'));

        moveForwardCorrectionBackInit(0);
        while(retrieveBNOData('p') < 25) {
            telemetry.addData("On: Floor Zone   BNO", retrieveBNOData('p'));
            moveForwardCorrectionBackground(1, 0.5, 0.1, 5, 10);
        }
        moveForwardCorrectionBackInit(0);
        while(retrieveBNOData('p') < 35) {
            telemetry.addData("On: Low Zone     BNO", retrieveBNOData('p'));
            moveForwardCorrectionBackground(1,0.35,0.1,5,10);
        }
        moveForwardCorrectionBackInit(0);
        while(true) {
            telemetry.addData("On: Mid Zone     BNO", retrieveBNOData('p'));
            moveForwardCorrectionBackground(4,0.25,0.1,5,10);
        }
    }
}