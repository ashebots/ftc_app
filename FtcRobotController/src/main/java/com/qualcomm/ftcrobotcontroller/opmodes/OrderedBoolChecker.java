package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class OrderedBoolChecker {
    //defines variables
    int step;
    public OrderedBoolChecker() {
        step = 0;
    }

    //if the boolean is true, it moves to the next step
    public void ifBool(boolean b) {
        if(b) {
            step++;
        }
    }

    public int getStep() {
        return step;
    }
}
