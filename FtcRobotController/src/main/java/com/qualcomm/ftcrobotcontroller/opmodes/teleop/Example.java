package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import com.qualcomm.ftcrobotcontroller.opmodes.complexOps.*;

/**
 * Created by apple on 9/6/16.
 */
public class Example extends AdvOpMode {
    public void init() {

        TriggerEvent b = new TriggerEvent();

        String state = b.parse(gamepad1.left_trigger);

        state = "UNPRESSED"; //The button is not pressed
        state = "PRESSED";   //The button was just pressed
        state = "HELD";      //The button is continuing to be pressed
        state = "RELEASED";  //The button was just released
    }
    public void loop() {

    }
}
