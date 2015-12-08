/*
=========================
AUTHOR = Art (2015-11-10)
=========================
*/

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.ftcrobotcontroller.opmodes.BNO055LIB;

public abstract class TestAutoMountain extends LinearOpMode
{
    DcMotor motorRight;
    DcMotor motorLeft;
    BNO055LIB boschBNO055;
    double distance;
    double goalAngle = -135;
    boolean leftMotorNeg;
    double roffset;
    double loffset;

    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];
    long systemTime;

    @Override
    public void runOpMode() throws InterruptedException
    {
        motorLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);

        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        systemTime = System.nanoTime();
        try {
            boschBNO055 = new BNO055LIB(hardwareMap, "bno055"

                    //The following was required when the definition of the "I2cDevice" class was incomplete.
                    //, "cdim", 5

                    , (byte)(BNO055LIB.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    //addressing
                    , (byte)BNO055LIB.OPERATION_MODE_IMU);
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception: " + e.getMessage());
        }
        Log.i("FtcRobotController", "IMU Init method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");

        waitForStart();

        systemTime = System.nanoTime();
        boschBNO055.startIMU();//Set up the IMU as needed for a continual stream of I2C reads.
        Log.i("FtcRobotController", "IMU Start method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");

        boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        telemetry.addData("Headings(yaw): ",
                String.format("Euler= %4.5f, Quaternion calculated= %4.5f", yawAngle[0], yawAngle[1]));
        telemetry.addData("Pitches: ",
                String.format("Euler= %4.5f, Quaternion calculated= %4.5f", pitchAngle[0], pitchAngle[1]));
        telemetry.addData("Max I2C read interval: ",
                String.format("%4.4f ms. Average interval: %4.4f ms.", boschBNO055.maxReadInterval
                        , boschBNO055.avgReadInterval));

        Thread.sleep(10000);

        motorLeft.setPower(.1);
        motorRight.setPower(.1);
        roffset = motorRight.getCurrentPosition();
        loffset = motorLeft.getCurrentPosition();
        while(motorLeft.getCurrentPosition()-loffset < distance) {
            telemetry.addData("Left Motor", motorLeft.getCurrentPosition()-loffset);
            telemetry.addData("Right Motor", motorRight.getCurrentPosition()-roffset);

            boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
            telemetry.addData("Headings(yaw): ",
                    String.format("Euler= %4.5f, Quaternion calculated= %4.5f", yawAngle[0], yawAngle[1]));
            telemetry.addData("Pitches: ",
                    String.format("Euler= %4.5f, Quaternion calculated= %4.5f", pitchAngle[0], pitchAngle[1]));
            telemetry.addData("Max I2C read interval: ",
                    String.format("%4.4f ms. Average interval: %4.4f ms.", boschBNO055.maxReadInterval
                            , boschBNO055.avgReadInterval));

            waitOneFullHardwareCycle();
        }

        boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        double goalYaw = yawAngle[0] + goalAngle;

        if(goalYaw < -180)
        {
            goalYaw = 360 + goalYaw;
        }

        if (leftMotorNeg) {
            motorRight.setPower(.1);
            motorLeft.setPower(-.1);
        } else {
            motorRight.setPower(-.1);
            motorLeft.setPower(.1);
        }
        while(yawAngle[0] > goalYaw - 10 || goalYaw + 10 < yawAngle[0]) {
            telemetry.addData("Left Motor", motorLeft.getCurrentPosition()-loffset);
            telemetry.addData("Right Motor", motorRight.getCurrentPosition()-roffset);

            boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
            telemetry.addData("Headings(yaw): ",
                    String.format("Euler= %4.5f, Quaternion calculated= %4.5f", yawAngle[0], yawAngle[1]));
            telemetry.addData("Pitches: ",
                    String.format("Euler= %4.5f, Quaternion calculated= %4.5f", pitchAngle[0], pitchAngle[1]));
            telemetry.addData("Max I2C read interval: ",
                    String.format("%4.4f ms. Average interval: %4.4f ms.", boschBNO055.maxReadInterval
                            , boschBNO055.avgReadInterval));

            waitOneFullHardwareCycle();
        }
        if (leftMotorNeg) {
            motorRight.setPower(0.01);
            motorLeft.setPower(-0.01);
        } else {
            motorRight.setPower(-0.01);
            motorLeft.setPower(0.01);
        }
        while(yawAngle[0] > goalYaw - 2.5 || goalYaw + 2.5 < yawAngle[0]) {
            telemetry.addData("Left Motor", motorLeft.getCurrentPosition()-loffset);
            telemetry.addData("Right Motor", motorRight.getCurrentPosition()-roffset);

            boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
            telemetry.addData("Headings(yaw): ",
                    String.format("Euler= %4.5f, Quaternion calculated= %4.5f", yawAngle[0], yawAngle[1]));
            telemetry.addData("Pitches: ",
                    String.format("Euler= %4.5f, Quaternion calculated= %4.5f", pitchAngle[0], pitchAngle[1]));
            telemetry.addData("Max I2C read interval: ",
                    String.format("%4.4f ms. Average interval: %4.4f ms.", boschBNO055.maxReadInterval
                            , boschBNO055.avgReadInterval));

            waitOneFullHardwareCycle();
        }
        motorRight.setPower(.1);
        motorLeft.setPower(.1);
    }
}

