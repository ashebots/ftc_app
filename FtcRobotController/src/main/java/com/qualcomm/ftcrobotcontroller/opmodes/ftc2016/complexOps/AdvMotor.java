package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class AdvMotor extends HardwareComponent {
    //defines hardware
    DcMotor motor;
    //defines variables
    double encOld = 0;
    double enc = 0;
    double offset = 0;
    //sets settings for hardware
    public AdvMotor(DcMotor m) {
        motor = m;
        motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    //SENSORS - control encoder's or sensor's relative (and absolute) positions.

    @Override
    public void calibrate() { //sets the current encoder value as 'old' such that getValues can see the difference
        encOld = motor.getCurrentPosition();
    }
    @Override
    public void getValues() {
        enc += motor.getCurrentPosition() - encOld;
    }

    //resets encoders
    public void resetEncs() {
        enc = 0;
        offset = encOld = motor.getCurrentPosition();
    }

    //BOOLEANS - return if a sensor value is in a range

    public boolean mRange(double targ, double range) { //Relative Range (the encoder value tracked while THIS OBJECT is moving the motor)
        return (Math.abs(enc) < targ+range && Math.abs(enc) > targ-range);
    }
    public boolean aRange(double targ, double range) { //Absolute Range (the actual encoder value)
        return (Math.abs(motor.getCurrentPosition()-offset) < targ+range && Math.abs(motor.getCurrentPosition()-offset) > targ-range);
    }

    //FUNCTIONS - move the object

    public String setMotor(double x) {
        motor.setPower(x);
        return "Moving motor";
    }

    public void stop() {
        motor.setPower(0);
    }
}
