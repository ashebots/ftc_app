package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.auto;

import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.AdvOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.IMUChassis;

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
