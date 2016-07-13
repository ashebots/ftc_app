package com.qualcomm.ftcrobotcontroller.opmodes.past.res_q.art;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class TimeManager{
    double systemTime = 0;
    double offset = 0;

    public void update() {
        systemTime = System.currentTimeMillis()-offset;
    }
    public boolean tRange (double targ, double range) {
        return (systemTime < targ+range && systemTime > targ-range);
    }
    public void resetTimer() {
        offset = System.currentTimeMillis();
    }
}
