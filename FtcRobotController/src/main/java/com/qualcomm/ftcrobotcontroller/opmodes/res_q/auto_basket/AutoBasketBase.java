package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket;


import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.ResQRobotBase;
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

    GyroSensor sensorGyro;

    @Override
    public void init()
    {
        //Initialize shared components/hardware
        super.init();

        //Initialize other autonomous specific hardware
        sensorGyro = hardwareMap.gyroSensor.get("sensorGyro");

        //Start calibrating gyro here
        sensorGyro.calibrate();
    }

    @Override
    public void start()
    {

    }

    @Override
    public void loop()
    {
        switch (programState)
        {
            case INITIALIZE:

                //Gyroscope is still calibrating, wait for it to finish.
                if (sensorGyro.isCalibrating())
                {
                    return;
                }

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
