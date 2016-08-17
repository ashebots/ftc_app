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
    boolean running = false;
    public AdvServo(Servo s) {
        servo = s;
    }

    //SENSORS - control encoder's or sensor's relative (and absolute) positions.

    @Override
    public void calibrate() { //sets the current encoder value as 'old' such that getValues can see the difference
        if (running) {
            posOld = servo.getPosition();
        }
    }
    @Override
    public void getValues() {
        if (running) {
            position += servo.getPosition() - posOld;
        }
    }
    //resets encoders
    public void resetEncs() {
        position = 0;
        offset = posOld = servo.getPosition();
    }

    //BOOLEANS - return if a sensor value is in a range

    public boolean sRange (double min, double max) { //relative
        return (position < max && position > min);
    }
    public boolean aRange (double min, double max) { //absolute
        return (servo.getPosition()-offset < max && servo.getPosition()-offset > min);
    }

    //FUNCTIONS - move the object

    public String setServo(double x) {
        running = true;
        servo.setPosition(x);
        return "Moving Servo";
    }
    @Override
    public void stop() {
        running = false;
        position = servo.getPosition();
    }
}
