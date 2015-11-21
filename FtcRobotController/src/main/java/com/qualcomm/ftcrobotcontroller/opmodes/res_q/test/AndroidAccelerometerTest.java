package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.AccelerometerSensor;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.ResQRobotBase;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.lang.reflect.Method;

public class AndroidAccelerometerTest extends OpMode
{
    Context appContext;
    SensorManager sensorManager;

    AccelerometerSensor sensorAccelerometer;

    @Override
    public void init()
    {
        appContext = getContext();
        sensorManager = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);

        sensorAccelerometer = new AccelerometerSensor(appContext, sensorManager, SensorManager.SENSOR_DELAY_FASTEST, 7.0f);
    }

    @Override
    public void loop()
    {
        if (sensorAccelerometer.isTipping())
        {
            ResQRobotBase.staticBeep(appContext);
        }
    }

    @Override
    public void stop()
    {
        sensorAccelerometer.unregister();
    }


    //Taken from LASA Robotics
    public static Context getContext() {
        try {
            final Class<?> activityThreadClass =
                    Class.forName("android.app.ActivityThread");
            //find and load the main activity method
            final Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (final Throwable e) {
            // handle exception
            return null;
        }
    }
}
