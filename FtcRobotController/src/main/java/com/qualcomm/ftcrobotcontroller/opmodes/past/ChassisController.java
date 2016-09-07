package com.qualcomm.ftcrobotcontroller.opmodes.past;

import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.AdvOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.Chassis;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.JoyEvent;
import com.qualcomm.ftcrobotcontroller.opmodes.teleop.complexOps.BoolEvent;

public class ChassisController extends AdvOpMode {
    Chassis chassis;
    JoyEvent n = new JoyEvent(0.4,0.55,0.35,0.2);
    JoyEvent s = new JoyEvent(0.08,0.11,0.35,0.2);
    BoolEvent p = new BoolEvent();
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
