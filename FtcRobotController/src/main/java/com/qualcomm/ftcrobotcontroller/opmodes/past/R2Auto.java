package com.qualcomm.ftcrobotcontroller.opmodes.past;

import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.AutoRoutine;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.IMUChassis;

/**
 * Created by apple on 8/18/16.
 */
public class R2Auto extends AutoRoutine {
    IMUChassis chassis;
    public R2Auto(IMUChassis c) {
        chassis = c;
    }
    public boolean states(int step) {
        switch(step) {
            case 0:
                chassis.setMotors(0.5);
                state.state(chassis.mRange(2000,INF),1);
                break;
            case 1:
                chassis.turnMotors(-0.5);
                state.state(chassis.aRange(-45,-180),2);
                break;
            case 2:
                chassis.setMotors(0.5);
                state.state(chassis.pRange(15,20),3);
                break;
            case 3:
                chassis.setMotors(0.5);
                if(chassis.pRange(0,5)) {
                    return true;
                }
                break;
        }
        return false;
    }
    public void getValues() {
        chassis.getValues();
    }
    public void calibrate() {
        chassis.calibrate();
    }
    public void between() {
        chassis.stop();
    }
    public void stop() {
        chassis.stop();
    }
}
