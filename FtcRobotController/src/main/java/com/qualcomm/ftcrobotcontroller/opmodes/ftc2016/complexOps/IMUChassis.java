package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class IMUChassis extends HardwareComponent{
    //defines hardware
    public DcMotor motorRight;
    public DcMotor motorLeft;
    public BNO055LIB imu;
    //defines variables
    double encLOld = 0;
    double encROld = 0;
    public double encoderLeft = 0;
    public double encoderRight = 0;
    public double loff = 0;
    public double roff = 0;
    boolean running = false;
    //bno angles
    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];
    long systemTime;
    //sets settings for hardware
    public IMUChassis(DcMotor l, DcMotor r, BNO055LIB b) {
        motorLeft = l;
        motorRight = r;
        imu = b;
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        systemTime = System.nanoTime();
        imu.startIMU();//Set up the IMU as needed for a continual stream of I2C reads.
    }

    //SENSORS - control encoder's or sensor's relative (and absolute) positions.

    @Override
    public void calibrate() { //sets the current encoder values as 'old' such that getValues can see the difference
        if (running) {
            encLOld = motorLeft.getCurrentPosition();
            encROld = motorRight.getCurrentPosition();
        }
    }
    @Override
    public void getValues() {
        if (running) {
            imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
            encoderLeft += motorLeft.getCurrentPosition() - encLOld;
            encoderRight += motorRight.getCurrentPosition() - encROld;
        }
    }
    //resets encoders
    public void resetEncs() {
        encoderLeft = 0;
        encoderRight = 0;
        loff = encLOld = motorLeft.getCurrentPosition();
        roff = encROld = motorRight.getCurrentPosition();
    }

    double aStand;
    public void setStandard(double angle) {
        aStand = angle;
    }

    //values
    public double angle() {
        return yawAngle[0];
    }
    public double pitch() {
        return pitchAngle[0];
    }

    //BOOLEANS - return if a sensor value is in a range

    public boolean aRange(double min, double max) {
        return (r(yawAngle[0]-aStand) < r(max) && r(yawAngle[0]-aStand) > r(min));
    }
    public boolean pRange(double min, double max) {
        return (pitchAngle[0] < max && pitchAngle[0] > min);
    }
    public boolean lRange(double min, double max) {
        return (Math.abs(encoderLeft) < max && Math.abs(encoderLeft) > min);
    }
    public boolean rRange(double min, double max) {
        return (Math.abs(encoderRight) < max && Math.abs(encoderRight) > min);
    }
    public boolean mRange(double min, double max) {
        return ((Math.abs(encoderLeft)+Math.abs(encoderRight))/2 < max && (Math.abs(encoderLeft)+Math.abs(encoderRight))/2 > min);
    }

    //FUNCTIONS - move the object

    //moves forward or back
    public String setMotors(double x) {
        running = true;
        motorLeft.setPower(x);
        motorRight.setPower(x);
        return "Moving forward";
    }

    //turns
    public String turnMotors(double x) {
        running = true;
        motorLeft.setPower(x);
        motorRight.setPower(-x);
        return "Turning";
    }
    public String moveMotors(double l, double r) {
        running = true;
        motorLeft.setPower(l);
        motorRight.setPower(r);
        return "Moving";
    }
    @Override
    public void stop() {
        motorLeft.setPower(0);
        motorRight.setPower(0);
        running = false;
    }

    //function used to convert a number into a valid angle.
    public double r(double i) {
        return ((i+180)%360)-180;
    }
}
