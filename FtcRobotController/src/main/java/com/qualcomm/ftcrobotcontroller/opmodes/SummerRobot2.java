package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jezebelquit on 7/26/16.
 */
public class SummerRobot2 extends OpMode {

    public Servo servo;
    public DcMotor left;
    public DcMotor right;
    public DcMotor arm;

    @Override
    public void init(){
        servo = hardwareMap.servo.get("The Servo");
        left = hardwareMap.dcMotor.get("The Left Wheel");
        right = hardwareMap.dcMotor.get("The Right Wheel");
        //blockDumper = hardwareMap.dcMotor.get("The Arm");
        left.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop(){

        //For the servo:
        if (gamepad1.y){
            servo.setPosition(1);
        }else if (gamepad1.a){
            servo.setPosition(0);
        }else {
            servo.setPosition(0.5);
        }

        //For the two motors:
        left.setPower(gamepad1.left_stick_y);
        right.setPower(gamepad1.right_stick_y);

        //telemetry.addData("Arm Motor Position",blockDumper.getCurrentPosition());



    }

    @Override
    public void stop(){
        servo.setPosition(0.5);
    }
}
