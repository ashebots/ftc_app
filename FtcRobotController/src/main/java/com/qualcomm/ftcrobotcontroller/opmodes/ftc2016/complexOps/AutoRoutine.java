package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

//import required things

//AutoRoutine is a special HardwareComponent used to create autonomous programs easily.
//It automatically runs provided steps and moves on from one when TRUE is inputted into the StateMachine.
//In between steps, it will run the Between function. You can run it like a normal component, using .run();
public abstract class AutoRoutine extends HardwareComponent{
    Timer timer = new Timer();
    StateMachine blCheck = new StateMachine();
    //unused status variable - can be called for telemetry
    String s;

    public boolean run() {
        //outputs step number
        s = "Idle";
        //action to execute after step switches
        if (blCheck.ifMoved()) {between();}
        //executes the action specified by the step number, as well as checking if it should move to the next step
        if (states()) {
            stop();
        }
        return true;
    }

    //shuts motors off or whatever
    public abstract void stop();
    public abstract boolean states();
    public abstract void getValues();
    public abstract void between();
    public abstract void calibrate();
    public int getStep() {
        return blCheck.getStep();
    }
    public void reset() {
        blCheck = new StateMachine();
    }

    public boolean buttonPressed(HardwareComponent h, String s) {
        if (s.equals("PRESSED")) {
            h.calibrate();
        } else if (s.equals("HELD")) {
            h.getValues();
            h.calibrate();
            return true;
        } else if (s.equals("RELEASED")) {
            h.stop();
        }
        return false;
    }
}