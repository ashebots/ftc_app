package com.qualcomm.ftcrobotcontroller.opmodes.auto;

import org.ashebots.ftcandroidlib.complexOps.AutoRoutine;
import org.ashebots.ftcandroidlib.complexOps.IMUChassis;

/**
 * Created by apple on 9/8/16.
 */
public class Straight extends AutoRoutine {
    IMUChassis c;
    public Straight(IMUChassis i) {
        c = i;
    }
    @Override
    public void stop() {
        c.stop();
    }
    @Override
    public void between() {
        c.stop();
    }
    @Override
    public boolean states(int step) {
        switch(step) {
            case 0:
                c.setMotors(1.0);
                c.setStandard(c.angle());
                state.state(c.aRange(3000,INF),1);
                break;
            case 1:
                c.turnMotors(1.0);
                state.state(c.ARange(-175,-180),0);
        }
        return false;
    }
    @Override
    public void getValues() {
        c.getValues();
    }
    @Override
    public void calibrate() {
        c.calibrate();
    }
}
