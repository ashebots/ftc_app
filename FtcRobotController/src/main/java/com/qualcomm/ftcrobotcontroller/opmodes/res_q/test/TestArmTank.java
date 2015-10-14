package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;

import android.text.method.Touch;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.drive.ChassisArcade;

public class TestArmTank extends OpMode
{
    DcMotor motorDriveLeft;
    DcMotor motorDriveRight;

    ChassisArcade chassis;


    DcMotor motorArmJoint1; //First arm joint, one closest to robot
    DcMotor motorArmJoint2; //Second arm join, one farthest from robot
    DcMotor motorArmSwivel; //Motor that turns the arm's lazy-susan base

    TouchSensor sensorTouchArmJoint1; //Sensor to detect when the first arm join is fully closed
    TouchSensor sensorTouchArmJoint2; //Sensor to detect when the second arm join is fully closed

    ArmJoint armJoint1;
    ArmJoint armJoint2;

    ArmSwivel armSwivel;

    public void init()
    {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");

        chassis = new ChassisArcade(motorDriveLeft, motorDriveRight);


        motorArmJoint1 = hardwareMap.dcMotor.get("motorArmJoint1");
        motorArmJoint2 = hardwareMap.dcMotor.get("motorArmJoint2");
        motorArmSwivel = hardwareMap.dcMotor.get("motorArmSwivel");

        sensorTouchArmJoint1 = hardwareMap.touchSensor.get("sensorTouchArmJoint1");
        sensorTouchArmJoint2 = hardwareMap.touchSensor.get("sensorTouchArmJoint2");

        armJoint1 = new ArmJoint(motorArmJoint1, sensorTouchArmJoint1, 1000, 0.5);
        armJoint2 = new ArmJoint(motorArmJoint2, sensorTouchArmJoint2, 1000, 0.5);

        armSwivel = new ArmSwivel(motorArmSwivel, -500, 500);
    }

    public void loop()
    {
        //Driving
        chassis.Drive(gamepad1.left_stick_x, gamepad1.left_stick_y * -1);

        //Both arm joints opening and closing
        armJoint1.Articulate(gamepad2.left_stick_y * -1);
        armJoint2.Articulate(gamepad2.right_stick_y * -1);

        armSwivel.LimitedSwivel((gamepad2.left_trigger * -1) + gamepad2.right_trigger);
    }


    //Used to represent swivelling base of arm. Note that there should only be one of these.
    class  ArmSwivel
    {
        DcMotor motor;
        int encoderLimitLeft;
        int encoderLimitRight;

        public ArmSwivel(DcMotor motor,int encoderLimitLeft, int encoderLimitRight)
         {
             this.motor = motor;
             this.encoderLimitLeft = encoderLimitLeft;
             this.encoderLimitRight = encoderLimitRight;
         }

        /* MUST BE CALLED EVERY LOOP
        power = -1 to 1
        Negative value means arm swivel turns left/counter-clockwise, and is limited by this.encoderLimitLeft
        Positive value means arm swivel turns right/clockwise, and is limited by this.encoderLimitRight
        Motor encoder's 0 position is assumed to mean that the arm is pointing straight ahead, this means
        that the arm should be pointed straight ahead when the robot starts.
        */
        public void LimitedSwivel(double power)
        {
            power = Range.clip(power,-1, 1);

            //We are going left
            if (power < 0)
            {
                //We have not reached the limit
                if (this.motor.getCurrentPosition() > this.encoderLimitLeft)
                {
                    this.motor.setPower(power);
                }
                else
                {
                    this.motor.setPower(0);
                }
            }
            //We are going right
            else if (power > 0)
            {
                //not reached limit
                if (this.motor.getCurrentPosition() < this.encoderLimitRight)
                {
                   this.motor.setPower(power);
                }
                else
                {
                    this.motor.setPower(0);
                }

            }
            else
            {
                this.motor.setPower(0);
            }
        }
    }


    //Used to represent each joint in the arm. There are two joints, so two of these objects. Different from swivel!
    class ArmJoint
    {
        DcMotor motor;
        TouchSensor sensorTouchClosed; //Triggered when the arm is fully closed

        int encoderLimit; //How far the motor can extend the arm in encoder ticks
        double powerModifier; //Power input is multiplied by this

        public ArmJoint(DcMotor motor, TouchSensor sensorTouchClosed, int encoderLimit, double powerModifier)
        {
            this.motor = motor;
            this.sensorTouchClosed = sensorTouchClosed;
            this.encoderLimit = encoderLimit;
            this.powerModifier = powerModifier;
        }


        /* MUST BE CALLED EVERY LOOP
        power = -1 to 1
        Positive value means arm joint extends, and is limited by encoderLimit
        Negative value means arm joint contracts, and is limited by sensorTouchClosed
        Motor's encoder is reset when sensorTouchClosed is touched
        */
        public void Articulate(double power)
        {
            power = Range.clip(power, -1.0, 1.0);

            if (power > 0)
            {
                if (this.motor.getCurrentPosition() < this.encoderLimit)
                {
                    this.motor.setPower(power);
                }
                else
                {
                    this.motor.setPower(0);
                }
            }
            else if (power < 0)
            {
                if (this.sensorTouchClosed.isPressed() == false)
                {
                    this.motor.setPower(power);
                }
                else
                {
                    this.motor.setPower(0);
                }
            }
            else
            {
                this.motor.setPower(0);
            }
        }
    }
}