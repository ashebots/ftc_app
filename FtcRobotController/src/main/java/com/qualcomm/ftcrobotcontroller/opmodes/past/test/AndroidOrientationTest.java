package com.qualcomm.ftcrobotcontroller.opmodes.past.test;


import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.lang.reflect.Method;

public class AndroidOrientationTest extends OpMode implements SensorEventListener
{
    SensorManager sensorManager;
    Sensor orientationSensor;
    Context appContext;

    @Override
    public void init()
    {
        appContext = getContext();

        sensorManager = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void loop()
    {

    }

    @Override
    public void stop()
    {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float azimuth_angle = event.values[0];
        float pitch_angle = event.values[1];
        float roll_angle = event.values[2];
        telemetry.addData("1: azimuth_angle = ", azimuth_angle);
        telemetry.addData("2: pitch_angle", pitch_angle);
        telemetry.addData("3: roll_angle", roll_angle);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        telemetry.addData("0: accuracy: ", accuracy);
    }



    //Taken from LASA Robotics
    public static Context getContext() {
        try {
            final Class<?> activityThreadClass =
                    Class.forName("android.app.ActivityThread");
            //find and load the main activity method
            final Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (final java.lang.Throwable e) {
            // handle exception
            return null;
        }
    }
}
