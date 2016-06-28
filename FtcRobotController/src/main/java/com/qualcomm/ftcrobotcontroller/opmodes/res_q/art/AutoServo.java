package com.qualcomm.ftcrobotcontroller.opmodes.res_q.art;

import android.util.Log;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Art Schell on 3/17/2016.
 */
public class AutoServo{
    Servo servo;
    double position;

    public AutoServo(Servo s) {
        servo = s;
    }
    public void getValues() {
        position = servo.getPosition();
    }
    public boolean sRange (double targ, double range) {
        return (position < targ+range && position > targ-range);
    }
    public String setServo(double x) {
        servo.setPosition(x);
        return "Moving Servo";
    }
}
