package com.qualcomm.ftcrobotcontroller.opmodes.complexOps;

//The core of the AutoRoutine object.
public class StateMachine {
    //defines variables
    boolean stop = false;
    int step;
    //It starts at step 0
    public StateMachine() {
        step = 0;
    }

    //if the boolean is true, it moves to the specified step
    public void state(boolean b, int sp) {
        if(b) {
            step = sp;
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
