package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket;


import android.util.Log;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib.AdafruitIMU;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib.IMU;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib.ImperialRoboticsBNO055;
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
        DRIVE_TO_WHITE_TAPE, //Follow the colored tape to get to the white tape that leads directly to the beacon
        DRIVE_TO_BEACON, //Follow the white tape to get to beacon, use gyro to stay straight. HOW DO WE DETERMINE DISTANCE?
    }
    ProgramState programState = ProgramState.INITIALIZE;


    public IMU imu;

    @Override
    public void init()
    {
        //Initialize shared components/hardware
        super.init();

        //Initialize other autonomous specific hardware

        //IMU
        try {
            imu = new ImperialRoboticsBNO055(new AdafruitIMU(hardwareMap, "bno055"
                    , (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    //addressing
                    , (byte)AdafruitIMU.OPERATION_MODE_IMU));
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception instantiating IMU: " + e.getMessage());
        }


    }

    @Override
    public void start()
    {
        super.start();

        //Set up IMU
        imu.start();
        imu.invertYaw(true);
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
