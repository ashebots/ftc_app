package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.AdvMotor;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.AutoRoutine;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.Chassis;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.IMUChassis;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

/**
 * Created by apple on 8/6/16.
 */
public class Straight extends AutoRoutine {
    IMUChassis chassis;
    Chassis turning;
    public Straight(IMUChassis c) {
        chassis = c;
        turning = new Chassis(c.motorLeft,c.motorRight);
    }
    @Override
    public boolean states(int step) {
        switch (step) {
            case 0:
                chassis.setMotors(0.5);
                state.state(chassis.aRange(-179,-5)||chassis.aRange(5,179),1);
                break;
            case 1:
                if (chassis.pitch()<0) {
                    turning.turnMotors(-0.1);
                } else {
                    turning.turnMotors(0.1);
                }
                state.state(chassis.aRange(-5,5),0);
                break;
        }
        return true;
    }
    @Override
    public void getValues() {
        chassis.getValues();
    }
    @Override
    public void calibrate() {
        chassis.calibrate();
    }
    @Override
    public void between() {
        chassis.stop();
    }
    @Override
    public void stop() {
        chassis.stop();
    }

    public void reset() {chassis.resetEncs();}

    public double distance() {
        return (chassis.encoderLeft+chassis.encoderRight)/2;
    }
    public boolean dRange(double min, double max) {
        return chassis.mRange(min,max);
    }
}
