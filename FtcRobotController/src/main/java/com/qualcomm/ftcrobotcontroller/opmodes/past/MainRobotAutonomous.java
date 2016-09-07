package com.qualcomm.ftcrobotcontroller.opmodes.past;

import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.AdvMotor;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.AdvOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.IMUChassis;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

/**
 * Created by apple on 8/6/16.
 */
public class MainRobotAutonomous extends AdvOpMode {
    MRAutoRoutine a;
    @Override
    public void init() {
        IMUChassis i = imuchassis("leftMotor", "rightMotor", "bno055");
        IrSeekerSensor ir = hardwareMap.irSeekerSensor.get("irSeeker");
        AdvMotor m = mtr("blockDump");
        a = new MRAutoRoutine(i, ir, m);
        telemetry.addData("Initialized",0);
    }
    @Override
    public void loop() {
        a.run();
    }
    @Override
    public void stop() {
        a.stop();
    }
}
