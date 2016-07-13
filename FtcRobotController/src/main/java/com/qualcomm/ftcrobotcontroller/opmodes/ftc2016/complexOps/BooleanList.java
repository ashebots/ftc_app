package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

//The core of the AutoRoutine object.
public class BooleanList {
    //defines variables
    boolean stop = false;
    int step;
    //It starts at step 0
    public BooleanList() {
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

    //Returns true when the step just increased.
    public boolean ifMoved () {
        boolean placeholder = stop;
        stop = false;
        return placeholder;
    }
}
