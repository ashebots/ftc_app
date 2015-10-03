package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TestRPM extends OpMode
{
    int ticksPerRevolution = 1120;
    double lastReadTime;
    int lastEncoderValue;

    DcMotor testMotor;

    public void init()
    {
        testMotor = hardwareMap.dcMotor.get("testMotor");

        lastReadTime = getRuntime();
        lastEncoderValue = testMotor.getCurrentPosition();
    }

    public void loop(){
        testMotor.setPower(gamepad1.left_stick_y);



        int encoderChange = testMotor.getCurrentPosition() - lastEncoderValue;
        double timeChange = getRuntime() - lastReadTime;

        //double motorRevolutions = encoderChange / ticksPerRevolution;
        //double motorRPM = (1 / timeChange) * motorRevolutions * 60;
        //double motorRPM = (((1 / timeChange) * encoderChange) / ticksPerRevolution) * 60;
        double motorRPM = (encoderChange / timeChange) * (1 / ticksPerRevolution);

        telemetry.addData("1: Joystick", gamepad1.left_stick_y);
        telemetry.addData("2: Motor RPM", motorRPM);
        telemetry.addData("3. encoderChange:", encoderChange);
        telemetry.addData("4. timeChange", timeChange);
        //telemetry.addData("5. motorRevolutions", motorRevolutions);

        lastReadTime = getRuntime();
        lastEncoderValue = testMotor.getCurrentPosition();
    }
}
