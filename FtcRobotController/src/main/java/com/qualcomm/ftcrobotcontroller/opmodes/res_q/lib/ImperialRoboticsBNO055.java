package com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib;


public class ImperialRoboticsBNO055 extends IMU
{
    AdafruitIMU imu;

    //"The following arrays contain both the Euler angles reported by the IMU (indices = 0) AND the
    // Tait-Bryan angles calculated from the 4 components of the quaternion vector (indices = 1)" -AlanG
    private volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];

    //The imu should already be instantiated, but not started
    public ImperialRoboticsBNO055(AdafruitIMU imu)
    {
        this.imu = imu;
    }

    @Override
    public void start()
    {
        imu.startIMU();
    }

    @Override
    public void readIMU()
    {
        imu.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);

        this.roll = rollAngle[0];
        this.pitch = pitchAngle[0];
        this.yaw = yawAngle[0];
    }
}
