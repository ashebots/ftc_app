package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by jezebelquit on 7/26/16.
 */
//"SummerRobot2" is "Cameron's robot"
public class SummerRobot2 extends OpMode {

    Servo sweeper;
    Servo basketRaiser;

    DcMotor left;
    DcMotor right;

    DcMotor arm;
    DcMotor flagRaiser;
    DcMotor winch;

    @Override
    public void init(){
        sweeper = hardwareMap.servo.get("sweeper");
        basketRaiser = hardwareMap.servo.get("basketRaiser");

        left = hardwareMap.dcMotor.get("leftWheel");
        right = hardwareMap.dcMotor.get("rightWheel");
        left.setDirection(DcMotor.Direction.REVERSE);

        arm = hardwareMap.dcMotor.get("arm");
        flagRaiser = hardwareMap.dcMotor.get("flagRaiser");
        winch = hardwareMap.dcMotor.get("winch");
    }
    @Override
    public void loop(){

        //For the sweeper servo
        if (gamepad1.right_bumper){
            sweeper.setPosition(1);
        }else if (gamepad1.right_trigger > 0.3){
            sweeper.setPosition(0);
        }else {
            sweeper.setPosition(0.5);
        }

        //For driving
        double leftPower = (gamepad1.left_stick_y * -1) - gamepad1.left_stick_x;
        double rightPower = (gamepad1.left_stick_y * -1) + gamepad1.left_stick_x;
        leftPower = Range.clip(leftPower, -1.0, 1.0);
        rightPower = Range.clip(rightPower, -1.0, 1.0);
        left.setPower(leftPower);
        right.setPower(rightPower);

        telemetry.addData("leftPower", leftPower);
        telemetry.addData("rightPower", rightPower);

        //telemetry.addData("Arm Motor Position",blockDumper.getCurrentPosition());


        //For the dumper arm
        arm.setPower(gamepad1.right_stick_y);


        //for the flag raiser
        if (gamepad1.dpad_up)
        {
            flagRaiser.setPower(1);
        }
        else if (gamepad1.dpad_down)
        {
            flagRaiser.setPower(-1);
        }
        else
        {
            flagRaiser.setPower(0);
        }


        //For the winch
        if (gamepad1.left_bumper)
        {
            winch.setPower(1);
        }
        else if (gamepad1.left_trigger > 0.3)
        {
            winch.setPower(-1);
        }
        else
        {
            winch.setPower(0);
        }

        //For the extra basket raiser
        if (gamepad1.y)
        {
            basketRaiser.setPosition(1);
        }
        else if (gamepad1.a)
        {
            basketRaiser.setPosition(0);
        }
        else
        {
            basketRaiser.setPosition(.5);
        }
    }

    @Override
    public void stop(){
        sweeper.setPosition(0.5);
    }
}
