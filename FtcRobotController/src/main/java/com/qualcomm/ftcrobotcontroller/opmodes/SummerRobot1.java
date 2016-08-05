package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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
        leftTread = hardwareMap.dcMotor.get("The Left Tread");
        rightTread = hardwareMap.dcMotor.get("The Right Tread");
        blockCollector = hardwareMap.dcMotor.get("The Block Collector");
        flagRaiser = hardwareMap.dcMotor.get("The Flag Lifter");
        blockDumper = hardwareMap.dcMotor.get("The Block Dumper");
        containerLifter = hardwareMap.servo.get("The Arm Lifter ");
        containerLifter.scaleRange(0.5,50);
        leftTread.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop(){
        //For the servo:
        if (gamepad1.y){
            blockCollector.setPower(1);
        }else if (gamepad1.a){
            blockCollector.setPower(-1);
        }else {
            blockCollector.setPower(0);
        }

        //For the two motors:
        leftTread.setPower(gamepad1.left_stick_y);
        rightTread.setPower(gamepad1.right_stick_y);

        //For the arm (Not finished yet):
        

        //The flag raiser
        if (gamepad1.right_trigger == 1){
            flagRaiser.setPower(1);
        }else{
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
