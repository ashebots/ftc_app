package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.teleop;

import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.*;

/**
 * Created by apple on 7/14/16.
 */
public class TankDrive extends AdvOpMode {
    Chassis c;
    @Override
    public void init() {
        c = chassis("motorDriveLeft","motorDriveRight");
    }
    JoyEvent j = new JoyEvent(1.0,1.0,0.0,0.2);
    @Override
    public void loop() {
        double[] v = j.calc(gamepad1.left_stick_x,gamepad1.left_stick_y);
        c.moveMotors(v[0],v[1]);
    }
    @Override
    public void stop() {
        c.stop();
    }
}
