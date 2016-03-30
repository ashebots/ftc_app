package com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib;


import android.util.Log;

public abstract class IMU
{
    protected double roll = 0;
    protected double pitch = 0;
    protected double yaw = 0;

    protected double continuousRoll = 0;
    protected double continuousPitch = 0;
    protected double continuousYaw = 0;


    //Any secondary initialization that doesn't fit in the constructor should go here.
    //This should be called during OpMode.start()
    public abstract void start();


    //This must be implemented by subclasses, depending on how the particular IMU works.
    //This should read back the latest values from the IMU, and store them in variables for later use.
    //Note that this should only be used internally, updateAngles() should be used externally.
    protected abstract void readIMU();


    //Updates angles from readIMU() implementation, then also calculates continuous angles
    //MUST BE CALLED FREQUENTLY, OR CONTINUOUS ANGLES MAY BE INVALID!!
    public void updateAngles()
    {
        //Before updating angles, store them to be used in calculation for continuous angles
        double oldRoll = roll;
        double oldPitch = pitch;
        double oldYaw = yaw;

        //Update "normal" angles
        try //I'm not sure if a try/catch is necessary, but these IMUs and their drivers seem to be somewhat volatile things.
        {
            readIMU();
        }
        catch (Exception e)
        {
            Log.i("FtcRobotController", "Exception while reading IMU: " + e.getMessage());
        }

        //Calculate continuous angles
        //TODO: calculate other continuous values
        continuousYaw += calculateCircularDelta(yaw, oldYaw, 360.0);
    }

    //This calculates the delta between two points on a "circular" number line that wraps/overflows back onto itself.
    /*
    It does this somewhat naively, by assuming that if the absolute value of the "basic delta" (currentValue - previousValue)
    is greater than half of degreesInCircle, then the delta must not actually be THAT big, and must instead have crossed the "tipping point".
    If you have a better way, plz halp.
     */
    protected double calculateCircularDelta(double currentValue, double previousValue, double degreesInCircle)
    {
        degreesInCircle = Math.abs(degreesInCircle);
        double halfCircle = degreesInCircle / 2.0;

        //Basic delta
        double delta = currentValue - previousValue;

        //Handle overflow cases in either direction
        if (delta < -halfCircle)
        {
            delta += degreesInCircle;
        }
        else if (delta > halfCircle)
        {
            delta -= degreesInCircle;
        }

        return delta;
    }



    public double getRoll()
    {
        return roll;
    }
    public double getPitch()
    {
        return pitch;
    }
    public double getYaw()
    {
        return yaw;
    }

    public double getContinuousYaw()
    {
        return continuousYaw;
    }
    /* NOT IMPLEMENTED :(
    public double getContinuousPitch()
    {

    }
    public double getContinuousRoll()
    {

    }
    */
}
