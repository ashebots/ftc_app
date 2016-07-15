package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.teleop;

import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.*;

/**
 * Created by apple on 7/14/16.
 */
public class UltrasonicTest extends AdvOpMode {
    Chassis motors;
    AdvUltrasonic sonar;
    String driveMode;
    @Override
    public void init() {
        motors = chassis("leftMotor","rightMotor");
        sonar = sonic("ultrasonic","Legacy Module 1",5);
    }
    boolean mode = false;
    BoolEvent t = new BoolEvent();
    JoyEvent j = new JoyEvent(0.1,0.1,0.0,0.2);
    @Override
    public void loop() {
        if (t.parse(gamepad1.a).equals("RELEASED")) {
            mode = !mode;
        }
        sonar.getValues();
        if (mode) { //move forward until a wall
            driveMode = "in Autonomous";
            if (sonar.distance<45 && sonar.distance!=0) {
                motors.turnMotors(0.05);
            } else {
                motors.setMotors(0.05);
            }
        } else {
            driveMode = "in TeleOp";
            double[] speeds = j.calc(gamepad1.left_stick_x,gamepad1.left_stick_y);
            motors.moveMotors(speeds[0],speeds[1]);
        }
        telemetry.addData("Mode",driveMode);
        telemetry.addData("Sensor",sonar.distance);
    }
    @Override
    public void stop() {
        motors.stop();
    }
}
