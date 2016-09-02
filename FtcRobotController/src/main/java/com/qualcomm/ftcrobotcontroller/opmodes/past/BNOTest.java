package com.qualcomm.ftcrobotcontroller.opmodes.past;

import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.*;

public class BNOTest extends AdvOpMode {
    IMUChassis c;
    @Override
    public void init() {
        c = imuchassis("motorLeft","motorRight","imu");
    }
    JoyEvent j = new JoyEvent(0.5,0.5,0.0,0.2);
    @Override
    public void loop() {
        double[] values = j.calc(gamepad1.left_stick_x,gamepad1.left_stick_y);
        c.moveMotors(values[1],values[0]);
        c.getValues();
        telemetry.addData("IMU Data",c.angle());
    }
    @Override
    public void stop() {
        c.stop();
    }
}
