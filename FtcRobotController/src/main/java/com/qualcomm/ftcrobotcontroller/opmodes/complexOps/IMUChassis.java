package com.qualcomm.ftcrobotcontroller.opmodes.complexOps;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class IMUChassis extends Chassis {
    //defines hardware
    public BNO055LIB imu;
    //bno angles
    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];
    long systemTime;
    //sets settings for hardware
    public IMUChassis(DcMotor l, DcMotor r, BNO055LIB b) {
        super(l,r);
        imu = b;
        systemTime = System.nanoTime();
        imu.startIMU();//Set up the IMU as needed for a continual stream of I2C reads.
    }

    //SENSORS - control encoder's or sensor's relative (and absolute) positions.

    double aStand;
    public void setStandard(double angle) {
        aStand = angle;
    }

    //values
    public double angle() {
        imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        return yawAngle[0];
    }
    public double pitch() {
        imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        return pitchAngle[0];
    }

    //BOOLEANS - return if a sensor value is in a range

    public boolean ARange(double min, double max) {
        imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        return (r(yawAngle[0]-aStand) < r(max) && r(yawAngle[0]-aStand) > r(min));
    }
    public boolean PRange(double min, double max) {
        imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        return (pitchAngle[0] < max && pitchAngle[0] > min);
    }

    //function used to convert a number into a valid angle.
    public double r(double i) {
        return ((i+180)%360)-180;
    }
}
