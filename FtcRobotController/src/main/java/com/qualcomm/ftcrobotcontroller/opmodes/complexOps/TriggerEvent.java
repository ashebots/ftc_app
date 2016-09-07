package com.qualcomm.ftcrobotcontroller.opmodes.complexOps;

//An object used to return four separate states of a button.
public class TriggerEvent {
    boolean old;
    public TriggerEvent() {
        old = false;
    }
    public String parse(double d) {
        boolean b = d!=0.0;
        String a = "UNPRESSED"; //The button is not pressed.
        if (b&&old) a = "HELD"; //The button is pressed.
        if (!b&&old) a = "RELEASED"; //The button just got released.
        if (b&&!old) a = "PRESSED"; //The button just got pressed
        old = b;
        return a;
    }
}
