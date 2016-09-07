package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.auto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * Created by Bill on 8/23/2016.
 */
public class TreadAutoEncoders extends OpMode{
    DcMotor leftTread;
    DcMotor rightTread;
    DcMotor blockDumper;
    Servo basketRaiser;
    IrSeekerSensor infraSearcher;

    @Override
    public void init(){
        rightTread = hardwareMap.dcMotor.get("rightTread");
        leftTread = hardwareMap.dcMotor.get("leftTread");
        blockDumper = hardwareMap.dcMotor.get("blockDumper");
        basketRaiser = hardwareMap.servo.get("containerLifter");
        infraSearcher = hardwareMap.irSeekerSensor.get ("irSeeker");

        leftTread.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop(){
        if (infraSearcher.signalDetected()){

        }

        telemetry.addData("Angle detected", infraSearcher.signalDetected());
        telemetry.addData("Angle Of The IR Beacon", infraSearcher.getAngle());
    }

    @Override
    public void stop(){
        leftTread.setPower(0);
        rightTread.setPower(0);
    }
}
