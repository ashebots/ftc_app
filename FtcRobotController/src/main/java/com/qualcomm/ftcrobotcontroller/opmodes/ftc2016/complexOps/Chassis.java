package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class Chassis extends HardwareComponent{
    //defines hardware
    public DcMotor motorRight;
    public DcMotor motorLeft;
    //defines variables
    double encLOld = 0;
    double encROld = 0;
    public double encoderLeft = 0;
    public double encoderRight = 0;
    public double loff = 0;
    public double roff = 0;
    //sets settings for hardware
    public Chassis(DcMotor l, DcMotor r) {
        motorLeft = l;
        motorRight = r;
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    //SENSORS - control encoder's or sensor's relative (and absolute) positions.

    @Override
    public void calibrate() { //sets the current encoder values as 'old' such that getValues can see the difference
        encLOld = motorLeft.getCurrentPosition();
        encROld = motorRight.getCurrentPosition();
    }
    @Override
    public void getValues() {
        encoderLeft += motorLeft.getCurrentPosition() - encLOld;
        encoderRight += motorRight.getCurrentPosition() - encROld;
    }
    //resets encoders
    public void resetEncs() {
        encoderLeft = 0;
        encoderRight = 0;
        loff = encLOld = motorLeft.getCurrentPosition();
        roff = encROld = motorRight.getCurrentPosition();
    }

    double aStand;
    public void setStandard(double angle) {
        aStand = angle;
    }

    //BOOLEANS - return if a sensor value is in a range

    public boolean lRange(double targ, double range) {
        return (Math.abs(encoderLeft) < targ+range && Math.abs(encoderLeft) > targ-range);
    }
    public boolean rRange(double targ, double range) {
        return (Math.abs(encoderRight) < targ+range && Math.abs(encoderRight) > targ-range);
    }
    public boolean mRange(double targ, double range) {
        return ((Math.abs(encoderLeft)+Math.abs(encoderRight))/2 < targ+range && (Math.abs(encoderLeft)+Math.abs(encoderRight))/2 > targ-range);
    }

    //FUNCTIONS - move the object

    //moves forward or back
    public String setMotors(double x) {
        motorLeft.setPower(x);
        motorRight.setPower(x);
        return "Moving forward";
    }

    //turns
    public String turnMotors(double x) {
        motorLeft.setPower(x);
        motorRight.setPower(-x);
        return "Turning";
    }
    public String moveMotors(double l, double r) {
        motorLeft.setPower(l);
        motorRight.setPower(r);
        return "Moving";
    }
    @Override
    public void stop() {
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
}
