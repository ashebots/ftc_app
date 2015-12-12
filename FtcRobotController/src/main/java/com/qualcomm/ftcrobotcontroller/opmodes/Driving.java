package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Art Schell on 12/8/2015.
 */

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.ftcrobotcontroller.opmodes.BNO055LIB;


public abstract class Driving extends LinearOpMode {
    //Turn angle degrees from current position

    DcMotor motorRight;
    DcMotor motorLeft;
    BNO055LIB boschBNO055;

    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];
    long systemTime;

    public void initMotors() {
        motorLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);

        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void initBNO055() {
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
    }

    public void systemTimeSetup() {
        systemTime = System.nanoTime();
        boschBNO055.startIMU();//Set up the IMU as needed for a continual stream of I2C reads.
        Log.i("FtcRobotController", "IMU Start method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");
    }

    public void readBNO() {
        boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        telemetry.addData("Headings(yaw): ",
                String.format("Euler= %4.5f, Quaternion calculated= %4.5f", yawAngle[0], yawAngle[1]));
        telemetry.addData("Pitches: ",
                String.format("Euler= %4.5f, Quaternion calculated= %4.5f", pitchAngle[0], pitchAngle[1]));
        telemetry.addData("Max I2C read interval: ",
                String.format("%4.4f ms. Average interval: %4.4f ms.", boschBNO055.maxReadInterval
                        , boschBNO055.avgReadInterval));
    }

    public void turnOnSpot(double angle, double range, double mPower, boolean turnRight) throws InterruptedException {
        if (turnRight) {
            motorRight.setPower(mPower);
            motorLeft.setPower(-mPower);
        } else {
            motorRight.setPower(-mPower);
            motorLeft.setPower(mPower);
        }
        while(yawAngle[0] > angle + range || angle - range > yawAngle[0]) {

            readBNO();
            waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

    public void turnOnSpotPID(double angle, double rangePID, double range, double mPowerFast, double mPowerSlow, boolean turnRight) throws InterruptedException {
        turnOnSpot(angle, rangePID, mPowerFast, turnRight);
        turnOnSpot(angle, range, mPowerSlow, turnRight);
    }

    public void moveForward(double distance, double power) throws InterruptedException {
        motorLeft.setPower(power);
        motorRight.setPower(power);
        double roffset = motorRight.getCurrentPosition();
        double loffset = motorLeft.getCurrentPosition();
        while(motorLeft.getCurrentPosition()-loffset < distance) {
            telemetry.addData("Left Motor", motorLeft.getCurrentPosition()-loffset);
            telemetry.addData("Right Motor", motorRight.getCurrentPosition()-roffset);

            readBNO();

            waitOneFullHardwareCycle();
        }
    }

    public void moveForwardPID(double distance, double trigger, double power, double powerSlow) throws InterruptedException {
        moveForward(distance - trigger, power);
        moveForward(trigger, powerSlow);
    }

    public void moveForwardCorrection(double distance, double power, double restorePower, double correctionPower, double correctTrigger, double correctRange) throws InterruptedException {
        double keepAtAngle = yawAngle[0];
        double roffset = motorRight.getCurrentPosition();
        double loffset = motorLeft.getCurrentPosition();
        while(motorLeft.getCurrentPosition()-loffset < distance) {
            telemetry.addData("Left Motor", motorLeft.getCurrentPosition() - loffset);
            telemetry.addData("Right Motor", motorRight.getCurrentPosition() - roffset);

            readBNO();

            motorLeft.setPower(power);
            motorRight.setPower(power);

            if (yawAngle[0] > keepAtAngle + correctTrigger || keepAtAngle - correctTrigger > yawAngle[0]) {
                turnOnSpotPID(keepAtAngle, correctTrigger, correctRange, restorePower, correctionPower, (keepAtAngle-yawAngle[0]>0));
            }
            waitOneFullHardwareCycle();
        }
    }
}
