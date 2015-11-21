package com.qualcomm.ftcrobotcontroller.opmodes.res_q.teleop;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.ResQRobotBase;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.drive.ChassisArcade;
import org.ashebots.ftcandroidlib.motor.Motor;

public class TeleOp extends ResQRobotBase
{
    ChassisArcade chassis;

    ArmJoint armJoint1;
    ArmJoint armJoint2;

    ArmSwivel armSwivel;

    @Override
    public void init()
    {
        //This will call the init() method in the parent "ResQRobot" class. That is the class
        //where we store and setup all our HARDWARE variables.
        super.init();


        chassis = new ChassisArcade(motorDriveLeft, motorDriveRight);

        armJoint1 = new ArmJoint(motorArmJoint1, sensorTouchArmJoint1, 200000, 0.5);
        armJoint2 = new ArmJoint(motorArmJoint2, sensorTouchArmJoint2, 200000, 0.5);

        armSwivel = new ArmSwivel(motorArmSwivel, -500, 500);
    }

    @Override
    public void loop()
    {
        //Driving
        float driveX = gamepad1.left_stick_x;
        float driveY = gamepad1.left_stick_y * -1;
        if (gamepad1.x == false)
        {
            driveX *= .6f;
            driveY *= .6f;
        }
        chassis.Drive(driveX, driveY);
        //brayden did this ^

        //Sweeper
        motorBoxSweeper.setPower(gamepad1.right_stick_y);

        //Arm swivel
        float swivelInput;
        if (gamepad1.dpad_left)
        {
            swivelInput = -1.0f;
        }
        else if (gamepad1.dpad_right)
        {
            swivelInput = 1.0f;
        }
        else
        {
            swivelInput = 0.0f;
        }
        armSwivel.LimitedSwivel(swivelInput);

        //Joint 1
        float jointInput1;
        if(gamepad1.left_bumper)
        {
            jointInput1 = 1.0f;
        }
        else if (gamepad1.left_trigger > 0.3f)
        {
            jointInput1 = -1.0f;
        }
        else
        {
            jointInput1 = 0f;
        }
        armJoint1.Articulate(jointInput1);


        //Joint 2
        //brayden did this
        float jointInput2;
        if(gamepad1.right_bumper)
        {
            jointInput2 = 1.0f;
        }
        else if (gamepad1.right_trigger > 0.3f)
        {
            jointInput2 = -1.0f;
        }
        else
        {
            jointInput2 = 0f;
        }
        armJoint2.Articulate(jointInput2);

        /*
        //Test, might not work...
        if (sensorAccelerometer.isTipping())
        {
            beep();
        }
        */

        //Telemetry
        telemetry.addData("1: Left drive encoder = ", motorDriveLeft.getCurrentPosition());
        telemetry.addData("2: Right drive encoder = ", motorDriveRight.getCurrentPosition());
        telemetry.addData("3: Arm swivel encoder = ", motorArmSwivel.getCurrentPosition());
        telemetry.addData("4: Arm joint 1 encoder = ", motorArmJoint1.getCurrentPosition());
        telemetry.addData("5: Arm joint 2 encoder = ", motorArmJoint2.getCurrentPosition());
    }


    //Used to represent swivelling base of arm. Note that there should only be one of these.
    class  ArmSwivel
    {
        Motor motor;
        int encoderLimitLeft;
        int encoderLimitRight;

        public ArmSwivel(Motor motor, int encoderLimitLeft, int encoderLimitRight)
         {
             this.motor = motor;
             this.encoderLimitLeft = encoderLimitLeft;
             this.encoderLimitRight = encoderLimitRight;

             this.motor.setCurrentPosition(0);
         }

        /* MUST BE CALLED EVERY LOOP
        power = -1.0 to 1.0
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
        Motor motor;
        TouchSensor sensorTouchClosed; //Triggered when the arm is fully closed

        int encoderLimit; //How far the motor can extend the arm in encoder ticks
        double powerModifier; //Power input is multiplied by this

        public ArmJoint(Motor motor, TouchSensor sensorTouchClosed, int encoderLimit, double powerModifier)
        {
            this.motor = motor;
            this.sensorTouchClosed = sensorTouchClosed;
            this.encoderLimit = encoderLimit;
            this.powerModifier = powerModifier;
        }


        /* MUST BE CALLED EVERY LOOP
        power = -1.0 to 1.0
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
                    this.motor.setCurrentPosition(0);
                }
            }
            else
            {
                this.motor.setPower(0);
            }
        }
    }
}