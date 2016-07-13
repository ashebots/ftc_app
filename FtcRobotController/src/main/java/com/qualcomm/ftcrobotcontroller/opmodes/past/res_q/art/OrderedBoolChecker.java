package com.qualcomm.ftcrobotcontroller.opmodes.past.res_q.art;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class OrderedBoolChecker {
    //defines variables
    boolean stop = false;
    int step;
    public OrderedBoolChecker() {
        step = 0;
    }

    //if the boolean is true, it moves to the next step
    public void ifBool(boolean b) {
        if(b) {
            step++;
            stop = true;
        }
    }

    public int getStep() {
        return step;
    }

    public boolean ifMoved () {
        boolean placeholder = stop;
        stop = false;
        return placeholder;
    }
}
