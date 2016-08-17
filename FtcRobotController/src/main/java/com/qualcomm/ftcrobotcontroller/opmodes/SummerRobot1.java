package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by jezebelquit on 7/26/16.
 */
public class SummerRobot1 extends OpMode {

    //The variables:
    public DcMotor leftTread;
    public DcMotor rightTread;
    public DcMotor blockCollector;
    public DcMotor flagRaiser;
    public DcMotor blockDumper;
    public Servo containerLifter;


    @Override
    public void init(){

        //The hardware maps:
        leftTread = hardwareMap.dcMotor.get("leftTread");
        rightTread = hardwareMap.dcMotor.get("rightTread");
        blockCollector = hardwareMap.dcMotor.get("blockCollector");
        flagRaiser = hardwareMap.dcMotor.get("flagRaiser");
        blockDumper = hardwareMap.dcMotor.get("blockDumper");
        containerLifter = hardwareMap.servo.get("containerLifter");
        //containerLifter.scaleRange(0.5,50);
        leftTread.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop(){
        //For driving
        double leftPower = (gamepad1.left_stick_y * -1) - gamepad1.left_stick_x;
        double rightPower = (gamepad1.left_stick_y * -1) + gamepad1.left_stick_x;
        leftPower = Range.clip(leftPower, -1.0, 1.0);
        rightPower = Range.clip(rightPower, -1.0, 1.0);
        leftTread.setPower(leftPower);
        rightTread.setPower(rightPower);

        telemetry.addData("leftPower", leftPower);
        telemetry.addData("rightPower", rightPower);


        //For the block collector
        if (gamepad1.left_bumper){
            blockCollector.setPower(1);
        }else if (gamepad1.left_trigger > 0.3){
            blockCollector.setPower(-1);
        }else {
            blockCollector.setPower(0);
        }


        //for the block container raiser
        if (gamepad1.right_bumper)
        {
            containerLifter.setPosition(1);
        }
        else if (gamepad1.right_trigger > 0.3)
        {
            containerLifter.setPosition(0);
        }
        else
        {
            containerLifter.setPosition(0.5);
        }

        //For the block dumper
        blockDumper.setPower(gamepad1.right_stick_y * -0.3);


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
    }

    @Override
    public void stop(){
        leftTread.setPower(0);
        rightTread.setPower(0);
        blockCollector.setPower(0);
        flagRaiser.setPower(0);
        blockDumper.setPower(0);
        containerLifter.setPosition(0.5);
    }
}
