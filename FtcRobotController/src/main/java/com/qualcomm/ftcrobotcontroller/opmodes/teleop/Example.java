package com.qualcomm.ftcrobotcontroller.opmodes.teleop;

import org.ashebots.ftcandroidlib.complexOps.*;

/**
 * Created by apple on 9/6/16.
 */
public class Example extends AdvOpMode {
    AdvMotor m;
    Chassis c;
    public void init() {

    }
    BoolEvent b;
    public void loop() {
        m.mRange(1000,INF);

        c.mRange(1000,INF);
        c.lRange(1000,INF);
        c.rRange(1000,INF);
    }
    public void stop() {
    }
}