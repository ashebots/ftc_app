package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class Timer extends HardwareComponent{
    double systemTime = 0;
    double oldT = 0;

    //The timer is only updated when these are called. In this way you can pause a timer.

    @Override
    public void getValues() {
        systemTime += System.currentTimeMillis() - oldT;
    }
    @Override
    public void calibrate() { oldT = System.currentTimeMillis(); }

    //if the timer has passed a value.

    public boolean tRange (double range) {
        return (systemTime > range);
    }

    //resets the timer

    public void resetTimer() {
        systemTime = 0;
    }
    @Override
    public void stop() {}
}
