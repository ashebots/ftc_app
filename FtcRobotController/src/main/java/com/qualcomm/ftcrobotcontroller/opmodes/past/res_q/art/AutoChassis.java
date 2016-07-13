package com.qualcomm.ftcrobotcontroller.opmodes.past.res_q.art;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class AutoChassis{
    //defines hardware
    DcMotor motorRight;
    DcMotor motorLeft;
    BNO055LIB imu;
    //definse variables
    double encLOld = 0;
    double encROld = 0;
    double encoderLeft = 0;
    double encoderRight = 0;
    //bno angles
    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];
    long systemTime;
    //sets settings for hardware
    public AutoChassis(DcMotor l, DcMotor r, BNO055LIB b) {
        motorLeft = l;
        motorRight = r;
        imu = b;
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        systemTime = System.nanoTime();
        imu.startIMU();//Set up the IMU as needed for a continual stream of I2C reads.
    }
    //updates encoders and bno
    public void getValues() {
        imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        encoderLeft += motorLeft.getCurrentPosition() - encLOld;
        encoderRight += motorRight.getCurrentPosition() - encROld;
        encLOld = motorLeft.getCurrentPosition();
        encROld = motorRight.getCurrentPosition();
    }
    //resets encoders
    public void resetEncs() {
        encoderLeft = 0;
        encoderRight = 0;
        encLOld = motorLeft.getCurrentPosition();
        encROld = motorRight.getCurrentPosition();
    }
    //values
    public double angle() {
        return yawAngle[0];
    }
    public double pitch() {
        return pitchAngle[0];
    }
    //booleans that the program can use
    public boolean aRange(double targ, double range) {
        return (yawAngle[0] < targ+range && yawAngle[0] > targ-range);
    }
    public boolean pRange(double targ, double range) {
        return (pitchAngle[0] < targ+range && pitchAngle[0] > targ-range);
    }
    public boolean lRange(double targ, double range) {
        return (Math.abs(encoderLeft) < targ+range && Math.abs(encoderLeft) > targ-range);
    }
    public boolean rRange(double targ, double range) {
        return (Math.abs(encoderRight) < targ+range && Math.abs(encoderRight) > targ-range);
    }
    public boolean mRange(double targ, double range) {
        return ((Math.abs(encoderLeft)+Math.abs(encoderRight))/2 < targ+range && (Math.abs(encoderLeft)+Math.abs(encoderRight))/2 > targ-range);
    }
    //moves forward or back
    public String setMotors(double x) {
        motorLeft.setPower(x);
        motorRight.setPower(x);
        return "Moving forward";
    }
    double aStand;
    public void setStandard(double angle) {
        aStand = angle;
    }
    //stays in a straight line
    public String straightMotors(double x, double y, double r) {
        String c = "Idle";
        if (!(aRange(-30+aStand,30-r)||aRange(30+aStand,30-r))) {
            c = setMotors(x);
        }
        if (aRange(-30+aStand,30-r)) {
            c = turnMotors(y);
        }
        if (aRange(30+aStand,30-r)) {
            c = turnMotors(-y);
        }
        return c;
    }
    //turns
    public String turnMotors(double x) {
        motorLeft.setPower(x);
        motorRight.setPower(-x);
        return "Turning";
    }

    public void stop() {
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
}
