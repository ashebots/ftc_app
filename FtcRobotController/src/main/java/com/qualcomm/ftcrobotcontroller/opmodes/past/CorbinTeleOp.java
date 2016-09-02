package com.qualcomm.ftcrobotcontroller.opmodes.past;

import com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps.*;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by apple on 8/30/16.
 */
public class CorbinTeleOp extends AdvOpMode {
    DcMotor left;
    DcMotor right;
    @Override
    public void init() {
        left = hardwareMap.dcMotor.get("motorLeft");
        right = hardwareMap.dcMotor.get("motorRight");
        left.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop() {
        left.setPower(gamepad1.left_stick_y);
        right.setPower(gamepad1.right_stick_y);
    }
    @Override
    public void stop() {
        left.setPower(0);
        right.setPower(0);
    }
}