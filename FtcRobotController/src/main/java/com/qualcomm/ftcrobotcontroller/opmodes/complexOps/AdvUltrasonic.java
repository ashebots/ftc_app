package com.qualcomm.ftcrobotcontroller.opmodes.complexOps;

import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by apple on 7/14/16.
 */
public class AdvUltrasonic extends HardwareComponent {
    LegacyModule legacy;
    UltrasonicSensor sonar;

    public AdvUltrasonic(UltrasonicSensor s, LegacyModule l, int port) {
        legacy = l;
        sonar = s;
        legacy.enable9v(port, true);
    }

    public double dist() {
        return sonar.getUltrasonicLevel();
    }
    public boolean sRange(double min, double max) {
        double distance = sonar.getUltrasonicLevel();
        return (distance>min && distance<max && distance!=0 && distance!=255);
    }
}
