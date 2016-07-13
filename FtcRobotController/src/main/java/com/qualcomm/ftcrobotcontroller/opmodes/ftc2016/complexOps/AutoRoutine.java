package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

//import required things

//AutoRoutine is a special HardwareComponent used to create autonomous programs easily.
//It automatically runs 10 steps and moves on from one when TRUE is inputted into the BooleanList.
//In between steps, it will run the Between function. You can run it like a normal component, using .run();
public abstract class AutoRoutine extends HardwareComponent{
    Timer timer = new Timer();
    BooleanList blCheck = new BooleanList();
    //unused status variable - can be called for telemetry
    String s;

    public boolean run() {
        //outputs step number
        s = "Idle";
        //action to execute after step switches
        if (blCheck.ifMoved()) {between();}
        //executes the action specified by the step number, as well as checking if it should move to the next step
        switch(blCheck.getStep()+1) {
            case 1: //move forward
                step1();
                break; case 2:
                step2();
                break; case 3:
                step3();
                break; case 4:
                step4();
                break; case 5:
                step5();
                break; case 6:
                step6();
                break; case 7:
                step7();
                break; case 8:
                step8();
                break; case 9:
                step9();
                break; case 10:
                step10();
                break; default: //has ended, return that it is done
                stop();return false;
        }
        return true;
    }

    //shuts motors off or whatever
    public abstract void stop();
    public abstract void step1();
    public abstract void step2();
    public abstract void step3();
    public abstract void step4();
    public abstract void step5();
    public abstract void step6();
    public abstract void step7();
    public abstract void step8();
    public abstract void step9();
    public abstract void step10();
    public abstract void getValues();
    public abstract void between();
    public abstract void calibrate();
    public int getStep() {
        return blCheck.getStep();
    }
    public void reset() {
        blCheck = new BooleanList();
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