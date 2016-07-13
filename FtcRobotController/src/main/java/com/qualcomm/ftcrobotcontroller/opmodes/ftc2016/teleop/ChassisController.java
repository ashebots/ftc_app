package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.teleop;

import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.AdvMotor;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.AdvOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.Chassis;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.IMUChassis;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.JoyEvent;
import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.PressEvent;

public class ChassisController extends AdvOpMode {
    Chassis chassis;
    JoyEvent n = new JoyEvent(0.5,0.4,0.35,0.2);
    JoyEvent s = new JoyEvent(0.1,0.08,0.35,0.2);
    PressEvent p = new PressEvent();
    int JoyMode = 0;
    double[] motorPowers;
    @Override
    public void init() {
        chassis = chassis("leftMoto","rightMoto");
    }
    @Override
    public void loop() {
        if (gamepad1.a) {
            motorPowers = s.calc(gamepad1.left_stick_x,gamepad1.left_stick_y);
        } else {
            motorPowers = n.calc(gamepad1.left_stick_x,gamepad1.left_stick_y);
        }
        String back = p.parse(gamepad1.back);
        if (back.equals("PRESSED")) {
            JoyMode = (JoyMode+1)%4; //direction of correction for joystick - fixes glitch
            n.joymode = s.joymode = JoyMode;
        }
        telemetry.addData("Left",motorPowers[1]);
        telemetry.addData("Right",motorPowers[1]);
        chassis.moveMotors(motorPowers[0],motorPowers[1]);
    }
    @Override
    public void stop() {
        chassis.stop();
    }
}
