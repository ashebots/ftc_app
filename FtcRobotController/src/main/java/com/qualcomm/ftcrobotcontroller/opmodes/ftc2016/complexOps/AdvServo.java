package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class AdvServo extends HardwareComponent {
    Servo servo;
    double position;
    double posOld;
    double offset;

    public AdvServo(Servo s) {
        servo = s;
    }

    //SENSORS - control encoder's or sensor's relative (and absolute) positions.

    @Override
    public void calibrate() { //sets the current encoder value as 'old' such that getValues can see the difference
        posOld = servo.getPosition();
    }
    @Override
    public void getValues() {
        position += servo.getPosition() - posOld;
    }
    //resets encoders
    public void resetEncs() {
        position = 0;
        offset = posOld = servo.getPosition();
    }

    //BOOLEANS - return if a sensor value is in a range

    public boolean sRange (double targ, double range) { //relative
        return (position < targ+range && position > targ-range);
    }
    public boolean aRange (double targ, double range) { //absolute
        return (servo.getPosition()-offset < targ+range && servo.getPosition()-offset > targ-range);
    }

    //FUNCTIONS - move the object

    public String setServo(double x) {
        servo.setPosition(x);
        return "Moving Servo";
    }
    @Override
    public void stop() {
        position = servo.getPosition();
    }
}
