package com.qualcomm.ftcrobotcontroller.opmodes.past;

import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.AdvMotor;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.AutoRoutine;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.IMUChassis;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.Straight;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

/**
 * Created by apple on 8/6/16.
 */
public class MRAutoRoutine extends AutoRoutine {
    IMUChassis chassis;
    IrSeekerSensor ir;
    AdvMotor blockDump;
    Straight straight;
    public MRAutoRoutine(IMUChassis c, IrSeekerSensor i, AdvMotor b) {
        chassis = c;
        ir = i;
        blockDump = b;
        straight = new Straight(c);
    }
    @Override
    public boolean states(int step) {
        switch (step) {
            case 0:
                state.state(ir.getStrength()<0.5, 2);
                straight.run();
                state.state(straight.dRange(250, 350),1);
                break;
            case 1:
                blockDump.setMotor(0.25);
                if (blockDump.mRange(600,700)) {
                    return false;
                }
                break;
            case 2:
                chassis.turnMotors(0.5);
                state.state(chassis.aRange(30,40),3);
                break;
            case 3:
                straight.run();
                state.state(ir.getStrength()>0.5, 4);
                break;
            case 4:
                chassis.turnMotors(-0.5);
                state.state(chassis.aRange(-95,-85),1);
                break;
        }
        return true;
    }
    @Override
    public void getValues() {
        chassis.getValues();
        blockDump.getValues();
    }
    @Override
    public void calibrate() {
        chassis.calibrate();
        blockDump.calibrate();
    }
    @Override
    public void between() {
        chassis.stop();
        blockDump.stop();
        chassis.setStandard(chassis.angle());
    }
    @Override
    public void stop() {
        chassis.stop();
        blockDump.stop();
    }
}
