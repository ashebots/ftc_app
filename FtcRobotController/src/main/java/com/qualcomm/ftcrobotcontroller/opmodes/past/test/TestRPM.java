package com.qualcomm.ftcrobotcontroller.opmodes.past.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class TestRPM extends OpMode
{
    double minSampleTime = 0.3;
    int ticksPerRevolution = 1120;

    double lastReadTime;
    int lastEncoderValue;

    DcMotor testMotor;

    public void init()
    {
        testMotor = hardwareMap.dcMotor.get("testMotor");
        testMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        lastReadTime = getRuntime();
        lastEncoderValue = testMotor.getCurrentPosition();
    }

    public void loop()
    {
        testMotor.setPower(gamepad1.left_stick_y / 2);


        if (gamepad1.left_bumper)
        {
            testMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
        else if (gamepad1.right_bumper)
        {
            testMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }


        int encoderChange = testMotor.getCurrentPosition() - lastEncoderValue;
        double timeChange = getRuntime() - lastReadTime;

        if (timeChange < minSampleTime)
        {
            return;
        }


        double motorRPM = (((1 / timeChange) * encoderChange) / ticksPerRevolution) * 60;
        //double motorRPM = (encoderChange / timeChange) * (1 / ticksPerRevolution);

        telemetry.addData("1: Joystick", gamepad1.left_stick_y);
        telemetry.addData("2: Motor RPM", motorRPM);
        telemetry.addData("3: Motor Power:", testMotor.getPower());
        telemetry.addData("4: RunMode", testMotor.getChannelMode());

        lastReadTime = getRuntime();
        lastEncoderValue = testMotor.getCurrentPosition();
    }
}
