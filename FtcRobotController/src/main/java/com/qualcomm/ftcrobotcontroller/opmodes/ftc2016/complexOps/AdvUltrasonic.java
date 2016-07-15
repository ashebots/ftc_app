package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by apple on 7/14/16.
 */
public class AdvUltrasonic extends HardwareComponent {
    LegacyModule legacy;
    UltrasonicSensor sonar;
    public double distance = 0;

    public AdvUltrasonic(UltrasonicSensor s, LegacyModule l, int port) {
        legacy = l;
        sonar = s;
        legacy.enable9v(port, true);
    }

    @Override
    public void getValues() {
        distance = sonar.getUltrasonicLevel();
    }

    public boolean sRange(double target, double range) {
        return (distance>target-range && distance<target+range && distance!=0 && distance!=255);
    }
}
