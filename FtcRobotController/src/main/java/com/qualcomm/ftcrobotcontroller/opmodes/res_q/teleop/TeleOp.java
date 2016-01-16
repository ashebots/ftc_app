package com.qualcomm.ftcrobotcontroller.opmodes.res_q.teleop;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.ResQRobotBase;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;
import org.ashebots.ftcandroidlib.drive.ChassisArcade;
import org.ashebots.ftcandroidlib.misc.Toggle;
import org.ashebots.ftcandroidlib.motor.Motor;

import java.util.Timer;

public class TeleOp extends ResQRobotBase
{
    ChassisArcade chassis;

    ArmJoint armJoint1;
    ArmJoint armJoint2;

    ArmSwivel armSwivel;

    Toggle driveOrientationToggle;

    AllianceColor ourAlliance = AllianceColor.UNKNOWN; //Enum can be BLUE or RED

    Servo servoLeverHitter; //hits levers that release climbers on mountain. only initialized if alliance selected
    Toggle leverHitterToggle; //will open when true
    double tempLeverHitterPos = 0.0; //temoporary/testing

    @Override
    public void init()
    {
        //This will call the init() method in the parent "ResQRobot" class. That is the class
        //where we store and setup all our HARDWARE variables.
        super.init();

        chassis = new ChassisArcade(motorDriveLeft, motorDriveRight);

        armJoint1 = new ArmJoint(motorArmJoint1, sensorTouchArmJoint1, 200000, 0.5);
        armJoint2 = new ArmJoint(motorArmJoint2, sensorTouchArmJoint2, 200000, 0.5);

        armSwivel = new ArmSwivel(motorArmSwivel, 1.0, -800, 800);

        driveOrientationToggle = new Toggle();

        leverHitterToggle = new Toggle();
    }


    //Called continuously after "init" button but before "play" button pressed
    @Override public void init_loop()
    {
        //Select alliance
        if (gamepad1.x) //blue button
        {
            ourAlliance = AllianceColor.BLUE;
        }

        if (gamepad1.b) //red button
        {
            ourAlliance = AllianceColor.RED;
        }

        telemetry.addData("_Alliance = ", ourAlliance);
    }


    @Override
    public void start()
    {
        //Initialize encoders
        motorArmSwivel.setCurrentPosition(0);
        motorArmJoint1.setCurrentPosition(0);
        motorArmJoint2.setCurrentPosition(0);

        //Get servo for hitting levers, if our alliance is selected
        if (ourAlliance == AllianceColor.BLUE || ourAlliance == AllianceColor.RED)
        {
            servoLeverHitter = hardwareMap.servo.get("servoLeverHitter");
        }
    }


    @Override
    public void loop()
    {
        //region === DRIVING === (Collapse this w/ Android Studio)
        float driveX = gamepad1.left_stick_x;
        float driveY = gamepad1.left_stick_y * -1;
        if (gamepad1.x == false) //motors default at 0.6, X button is "nitro" to 1.0
        {
            driveX *= .6f;
            driveY *= .6f;
        }
        //Toggle driving orientation
        driveOrientationToggle.toggleState(gamepad1.a);
        if (driveOrientationToggle.getState()) //reverse driving orientation
        {
            driveY *= -1;
        }
        chassis.Drive(driveX, driveY);
        telemetry.addData("Left motor power = ", motorDriveLeft.getPower());
        telemetry.addData("Right motor power = ", motorDriveRight.getPower());
        //endregion


        //region === SWEEPER === (Collapse this w/ Android Studio)
        //(continuous, 0.5 is stop)
        boxSweeperPower = gamepad1.right_stick_y * -1;
        boxSweeperPower = (boxSweeperPower + 1) / 2; //Scales number from (-1 to +1) to (0 to 1) with 0.5 being default
        boxSweeperPower = Range.clip(boxSweeperPower, 0, 1);
        servoBoxSweeper.setPosition(boxSweeperPower);
        //endregion


        //region === LEVER HITTER SERVO ===
        /*
        if (ourAlliance != AllianceColor.UNKNOWN)
        {
            //Decide values for position
            double leverHitterOpenPos = 0;
            double leverHitterClosePos = 0;
            if (ourAlliance == AllianceColor.BLUE)
            {
                //made up values
                leverHitterOpenPos = 0.4;
                leverHitterClosePos = 0.6;
            }
            else if (ourAlliance == AllianceColor.RED)
            {
                //made up values
                leverHitterOpenPos = 0.4;
                leverHitterClosePos = 0.6;
            }

            //Decide to toggle
            leverHitterToggle.toggleState(gamepad1.y);

            //Decide position/set position
            if (leverHitterToggle.getState() == true) //open
            {
                servoLeverHitter.setPosition(leverHitterOpenPos);
            }
            else if (leverHitterToggle.getState() == false) //close
            {
                servoLeverHitter.setPosition(leverHitterClosePos);
            }
        }
        */

        //testing
        //if (ourAlliance == AllianceColor.UNKNOWN) {
            if (gamepad2.dpad_up)
                tempLeverHitterPos += 0.001;
            if (gamepad2.dpad_down)
                tempLeverHitterPos -= 0.001;

            tempLeverHitterPos = Range.clip(tempLeverHitterPos, 0.0, 1.0);
            servoLeverHitter.setPosition(tempLeverHitterPos);

            telemetry.addData("lever hitter target pos", tempLeverHitterPos);
            telemetry.addData("__lever hitter pos = ", servoLeverHitter.getPosition());
        //}

        //endregion


        //region === ARM SWIVEL === (Collapse this w/ Android Studio)
        float swivelInput;
        if (gamepad1.dpad_left)
        {
            swivelInput = -1.3f;
        }
        else if (gamepad1.dpad_right)
        {
            swivelInput = 1.3f;
        }
        else
        {
            swivelInput = 0.0f;
        }
        armSwivel.LimitedSwivel(swivelInput);
        //endregion

        //region === JOINT 1 === (Collapse this w/ Android Studio)
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
        //endregion

        //region === JOINT 2 === (Collapse this w/ Android Studio)
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
        //endregion

        //Quick little hack to tune the PID controller for the arm swivel
        //tunePID(armSwivelHeadingSettings);

        //region === TELEMETRY === (Collapse this w/ Android Studio)

        telemetry.addData("_Alliance = ", ourAlliance);
        //telemetry.addData("1: Left drive encoder = ", motorDriveLeft.getCurrentPosition());
        //telemetry.addData("2: Right drive encoder = ", motorDriveRight.getCurrentPosition());
        telemetry.addData("4: Arm swivel encoder = ", motorArmSwivel.getCurrentPosition());
        telemetry.addData("5: Arm joint 1 encoder = ", motorArmJoint1.getCurrentPosition());
        telemetry.addData("6: Arm joint 2 encoder = ", motorArmJoint2.getCurrentPosition());
        //endregion
    }


    //Used to represent swivelling base of arm. Note that there should only be one of these.
    class  ArmSwivel
    {
        Motor motor;
        double motorPower;

        int encoderLimitLeft;
        int encoderLimitRight;

        double currentEncoderTarget;

        public ArmSwivel(Motor motor, double motorPower, int encoderLimitLeft, int encoderLimitRight)
         {
             this.motor = motor;

             this.motorPower = Range.clip(motorPower, 0.0, 1.0);

             this.encoderLimitLeft = encoderLimitLeft;
             this.encoderLimitRight = encoderLimitRight;

             this.currentEncoderTarget = motor.getCurrentPosition();


             this.motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
         }

        /* MUST BE CALLED EVERY LOOP
        power = -1.0 to 1.0
        Negative value means arm swivel turns left/counter-clockwise, and is limited by this.encoderLimitLeft
        Positive value means arm sivel turns right/clockwise, and is limited by this.encoderLimitRight
        Motor encoder's 0 position is assumed to mean that the arm is pointing straight ahead, this means
        that the arm should be pointed straight ahead when the robot starts.
        */
        public void LimitedSwivel(double positionChangeInput)
        {
            positionChangeInput = Range.clip(positionChangeInput, -10.0, 10.0);

            //We are going left
            if (positionChangeInput < 0)
            {
                //We have not reached the limit
                if (this.currentEncoderTarget > this.encoderLimitLeft)
                {

                    this.currentEncoderTarget += positionChangeInput;
                }
            }
            //We are going right
            else if (positionChangeInput > 0)
            {
                //not reached limit
                if (this.currentEncoderTarget < this.encoderLimitRight)
                {
                   this.currentEncoderTarget += positionChangeInput;
                }

            }

            telemetry.addData("Swivel change input = ", positionChangeInput);
            telemetry.addData("current swivel target = ", this.currentEncoderTarget);

            //Now we have determined our new target position, try to get there
            this.motor.setMode(DcMotorController.RunMode.RUN_TO_POSITION); //This is redundant, just want to make sure we're in the right mode
            this.motor.setTargetPosition( (int) this.currentEncoderTarget);
            this.motor.setPower(this.motorPower);
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