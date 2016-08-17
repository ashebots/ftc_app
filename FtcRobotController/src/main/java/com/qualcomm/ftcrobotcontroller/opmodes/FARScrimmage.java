package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;

/**
 * Created by TeamFAR on 8/26/2015.
 */
public class FARScrimmage extends OpMode {

    // Set up the servo's range
    final static double TEST_SERVO_MIN_RANGE = 0.10;
    final static double TEST_SERVO_MAX_RANGE = 1.00;

    // Set up the "step" of the servo, or how far it turns with each increment
    double testServoTurn = 0.01;

    // Define the position variable
    double testServoPosition;
    double servoPosLH_Up = 1;
    double servoPosLH_Dn = 0;
    double servoPosRH_Up = 1;
    double servoPosRH_Dn = 0;

    // Define the servo motor
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorWinch;
    DcMotor motorTractorBeam;
    DcMotor motorFlagRaiser;
    Servo testServo; //Tape Azimuth

    @Override
    public void init() {
        testServo = hardwareMap.servo.get("servo_1");  //Tape Azimuth
        testServoPosition = .01;
        motorTractorBeam = hardwareMap.dcMotor.get("tractorBeam");
        motorWinch = hardwareMap.dcMotor.get("winch");
        motorRight1 = hardwareMap.dcMotor.get("motor_2");
        motorLeft1 = hardwareMap.dcMotor.get("motor_1");
        motorFlagRaiser = hardwareMap.dcMotor.get("flagRaiser");
        motorLeft1.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        double rightPower = (gamepad1.right_stick_y) / 1.5;
        double leftPower = (gamepad1.left_stick_y) / 1.5;
        motorRight1.setPower(rightPower);
        //motorRight2.setPower(rightPower);
        motorLeft1.setPower(leftPower);
        //motorLeft2.setPower(leftPower);

        if (gamepad1.right_trigger > 0.2 || gamepad1.right_bumper) {
            if (gamepad1.right_trigger > 0.2) {
                motorWinch.setPower(1);
            }
            if (gamepad1.right_bumper) {
                motorWinch.setPower(-1);
            }

        } else {

            motorWinch.setPower(0.0d);
        }

        if (gamepad1.left_trigger > 0.2 || gamepad1.left_bumper) {

            if (gamepad1.left_trigger > 0.2) {
                motorTractorBeam.setPower(.5);
            }

            if (gamepad1.left_bumper) {
                motorTractorBeam.setPower(-.5);
            }
        } else {

            motorTractorBeam.setPower(0.0d);
        }

        if (gamepad1.dpad_down) {
            testServoPosition = testServoPosition - testServoTurn;
        }
        if (gamepad1.dpad_up) {
            testServoPosition = testServoPosition + testServoTurn;
        }

        if (gamepad1.dpad_left || gamepad1.dpad_right) {
            if (gamepad1.dpad_right) {
                motorFlagRaiser.setPower(.5);
            }

            if (gamepad1.dpad_left) {
                motorFlagRaiser.setPower(-.5);
            }
        } else {

            motorFlagRaiser.setPower(0.0d);
        }



    testServoPosition=Range.clip(testServoPosition,TEST_SERVO_MIN_RANGE,TEST_SERVO_MAX_RANGE);
    testServo.setPosition(testServoPosition);
    }


    @Override
    public void stop() {

    }
}
