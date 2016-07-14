package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

//An object used to return four separate states of a button.
public class BoolEvent {
    boolean old;
    public BoolEvent() {
        old = false;
    }
    public String parse(boolean b) {
        String a = "UNPRESSED"; //The button is not pressed.
        if (b&&old) a = "HELD"; //The button is pressed.
        if (!b&&old) a = "RELEASED"; //The button just got released.
        if (b&&!old) a = "PRESSED"; //The button just got pressed
        old = b;
        return a;
    }
}
