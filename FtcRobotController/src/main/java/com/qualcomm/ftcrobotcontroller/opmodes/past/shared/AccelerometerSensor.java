package com.qualcomm.ftcrobotcontroller.opmodes.past.shared;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerSensor implements SensorEventListener
{
    private Context appContext;
    private SensorManager sensorManager;
    private int sensorDelay;

    private Sensor accelerometerSensor;

    public float[] accelAxes = new float[3];;

    private float tippingPoint;

    public AccelerometerSensor(Context appContext, SensorManager sensorManager, int sensorDelay, float tippingPoint)
    {
        this.appContext = appContext;
        this.sensorManager = sensorManager;
        this.sensorDelay = sensorDelay;
        this.setTippingPoint(tippingPoint);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, accelerometerSensor, sensorDelay);
    }

    //Check if the absolute value of the Y axis of the sensor is greater than the tipping point
    public boolean isTipping()
    {
        if (Math.abs(accelAxes[1]) > this.getTippingPoint())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        accelAxes = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //telemetry.addData("0: accuracy: ", accuracy);
    }

    public void unregister()
    {
        sensorManager.unregisterListener(this);
    }


    public void setTippingPoint(float point)
    {
        this.tippingPoint = Math.abs(point);
    }
    public float getTippingPoint()
    {
        return this.tippingPoint;
    }
}
