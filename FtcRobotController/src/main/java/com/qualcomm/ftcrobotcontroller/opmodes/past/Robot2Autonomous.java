package com.qualcomm.ftcrobotcontroller.opmodes.past;

import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.AdvOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.IMUChassis;

/**
 * Created by apple on 8/18/16.
 */
public class Robot2Autonomous extends AdvOpMode {
    R2Auto a;
    public void init() {
        IMUChassis chassis = imuchassis("motorLeft","motorRight","bno055");
        a = new R2Auto(chassis);
    }
    public void loop() {
        a.run();
    }
    public void stop() {
        a.stop();
    }
}
