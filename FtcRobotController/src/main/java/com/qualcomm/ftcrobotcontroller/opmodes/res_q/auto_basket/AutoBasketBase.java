package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket;


import android.util.Log;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib.AdafruitIMU;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.ResQRobotBase;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.GyroSensor;

public abstract class AutoBasketBase extends ResQRobotBase
{
    enum ProgramState
    {
        INITIALIZE,
        DRIVE_AWAY_FROM_WALL, //Use encoders
        ROTATE_TOWARDS_BEACON, //Use gyro
        DRIVE_TO_COLORED_TAPE, //Use gyro/encoders to get to colored floor goal/repair zone tape
        DRIVE_TO_WHITE_TAPE, //Follor the colored tape to get to the white tape that leads directly to the beacon
        DRIVE_TO_BEACON, //Follow the white tape to get to beacon, use gyro to stay straight. HOW DO WE DETERMINE DISTANCE?
    }
    ProgramState programState = ProgramState.INITIALIZE;


    AdafruitIMU imu;
    //The following arrays contain both the Euler angles reported by the IMU (indices = 0) AND the
    // Tait-Bryan angles calculated from the 4 components of the quaternion vector (indices = 1)
    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];
    //(Explanation of "volatile" here: http://tutorials.jenkov.com/java-concurrency/volatile.html)

    @Override
    public void init()
    {
        //Initialize shared components/hardware
        super.init();

        //Initialize other autonomous specific hardware

        //IMU
        try {
            imu = new AdafruitIMU(hardwareMap, "bno055"

                    //The following was required when the definition of the "I2cDevice" class was incomplete.
                    //, "cdim", 5

                    , (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    //addressing
                    , (byte)AdafruitIMU.OPERATION_MODE_IMU);
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception: " + e.getMessage());
        }


    }

    @Override
    public void start()
    {
        //Set up IMU
        imu.startIMU();
    }

    @Override
    public void loop()
    {
        switch (programState)
        {
            case INITIALIZE:

                //Can we make sure IMU is calibrated here??

                break;


            case DRIVE_AWAY_FROM_WALL:



                break;
        }

    }

    @Override
    public void stop()
    {

    }
}
