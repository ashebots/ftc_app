package com.qualcomm.ftcrobotcontroller.opmodes.past.teleop;

import com.qualcomm.ftcrobotcontroller.opmodes.past.shared.ResQRobotBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.control.PIDController;
import org.ashebots.ftcandroidlib.control.PIDSettings;
import org.ashebots.ftcandroidlib.drive.ChassisArcade;
import org.ashebots.ftcandroidlib.misc.Toggle;
import org.ashebots.ftcandroidlib.motor.Motor;


public class TeleOp extends ResQRobotBase
{
    ChassisArcade chassis;

    Toggle driveOrientationToggle;
    Toggle rotatedDriveToggle;

    PIDSettings armPIDSettings;
    ArmController armController;

    AllClearGrabber leftAllClearGrabber;
    AllClearGrabber rightAllClearGrabber;

    LeverHitter leverHitterLeft;
    LeverHitter leverHitterRight;

    @Override
    public void init()
    {
        //This will call the init() method in the parent "ResQRobot" class. That is the class
        //where we store and setup all our HARDWARE variables.
        super.init();

        chassis = new ChassisArcade(motorDriveLeft, motorDriveRight);

        driveOrientationToggle = new Toggle();
        rotatedDriveToggle = new Toggle();

        armPIDSettings = new PIDSettings(0.001, 0, 0);
        armController = new ArmController(motorArm, armPIDSettings, telemetry);

        leftAllClearGrabber = new AllClearGrabber(servoAllClearLeft);
        rightAllClearGrabber = new AllClearGrabber(servoAllClearRight);

        leverHitterLeft = new LeverHitter("Left", servoLeverHitterLeft, 0.36, 0.99);
        leverHitterRight = new LeverHitter("Right", servoLeverHitterRight, 0.22, 0.63);
    }




    @Override
    public void loop()
    {
        //TUNE ARM PID:
        //tunePID(armPIDSettings);


        //region === DRIVING === (Collapse this w/ Android Studio)
        
        float driveX = gamepad1.left_stick_x;
        float driveY = gamepad1.left_stick_y * -1;

        if (gamepad2.left_trigger > 0.5 && gamepad2.right_trigger > 0.5)
        {
            driveX *= .4f;
            driveY *= .4f;
        }
        else if (gamepad1.x == false) //motors default at 0.6, X button is "nitro" to 1.0
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
        //Toggle rotated driving fix
        rotatedDriveToggle.toggleState(gamepad1.b);
        if (rotatedDriveToggle.getState())
        {
            motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
            telemetry.addData("Drive rotation fix:", "ENABLED");
        }
        else
        {
            motorDriveLeft.setDirection(DcMotor.Direction.FORWARD);
            telemetry.addData("Drive rotation fix:", "DISABLED");
        }
        //Drive
        chassis.Drive(driveX, driveY);
        //telemetry.addData("Left motor power = ", motorDriveLeft.getPower());
        //telemetry.addData("Right motor power = ", motorDriveRight.getPower());

        //endregion


        //region === ARM === (Collapse this w/ Android Studio)

        double armInput = gamepad1.right_stick_y * -1; //Get joystick input and correct direction
        double armDelta = easeInCirc(Math.abs(armInput), 0, 1, 1); //Get an (absolute) "eased" value from 0 to 1. It rises slowly at first then rises steeply at the end
        armDelta *= Math.signum(armInput); //signum = 1 if input positive, -1 if negative, 0 if zero. This turns back to negative if input negative.

        armController.loop(armDelta, gamepad1.y);

        //endregion


        //region === CLIMBER DUMPER

        double climberDumperPower = 0.5;
        if (gamepad1.right_bumper)
        {
            climberDumperPower += 0.1;
        }
        if (gamepad1.right_trigger > 0.5)
        {
            climberDumperPower -= 0.1;
        }
        servoClimberDumper.setPosition(climberDumperPower);

        //endregion


        //region === PLOW

        double plowPower = 0.5;
        if (gamepad1.left_bumper)
        {
            plowPower += 0.2;
        }
        if (gamepad1.left_trigger > 0.5)
        {
            plowPower -= 0.2;
        }
        servoPlow.setPosition(plowPower);

        //endregion


        //region === ALL CLEAR GRABBERS

        //While holding b, sides are NOT synced
        if (gamepad2.b) //true means both are synced
        {
            //LEFT all clear
            leftAllClearGrabber.loop(gamepad2.left_bumper, gamepad2.left_trigger);
            //RIGHT all clear
            rightAllClearGrabber.loop(gamepad2.right_bumper, gamepad2.right_trigger);
        }
        else //If not holding b, then synced
        {
            //LEFT all clear
            leftAllClearGrabber.loop(gamepad2.right_bumper, gamepad2.right_trigger);
            //RIGHT all clear
            rightAllClearGrabber.loop(gamepad2.right_bumper, gamepad2.right_trigger);
        }

        //endregion


        //region === LOCKING BAR

        if (gamepad2.dpad_up)
        {
            motorLockingBar.setPower(1);
        }
        else if (gamepad2.dpad_down)
        {
            motorLockingBar.setPower(-1);
        }
        else
        {
            motorLockingBar.setPower(0);
        }

        //endregion


        //region === LEVER HITTER SERVOS ===

        leverHitterLeft.loop(gamepad2.left_stick_y * -1, gamepad2.y);
        leverHitterRight.loop(gamepad2.right_stick_y * -1, gamepad2.y);

        //endregion

        //region === TELEMETRY === (Collapse this w/ Android Studio)

        telemetry.addData("1: Left lever hitter pos = ", servoLeverHitterLeft.getPosition());
        telemetry.addData("2: Right lever hitter pos = ", servoLeverHitterRight.getPosition());

        //telemetry.addData("3: Left plow pos = ", servoPlowLeft.getPosition());
        //telemetry.addData("4: Right plow pos = ", servoPlowRight.getPosition());

        //telemetry.addData("5: Left drive encoder = ", motorDriveLeft.getCurrentPosition());
        //telemetry.addData("6: Right drive encoder = ", motorDriveRight.getCurrentPosition());

        //endregion

    }
}


class ArmController
{
    Motor armMotor;

    PIDSettings pidSettings;
    PIDController pidController;

    Telemetry telemetry;


    double currentEncoderTarget; //This is intentionally a double, not an int
    double encoderTargetRange = 100; //currentEncoderTarget is Range.clip()'d to be a maximum of this far away from armMotor.getCurrentPosition() in either direction


    public ArmController(Motor armMotor, PIDSettings pidSettings, Telemetry telemetry)
    {
        this.armMotor = armMotor;
        this.currentEncoderTarget = armMotor.getCurrentPosition();

        this.pidSettings = pidSettings;
        this.pidController = new PIDController(pidSettings);

        this.telemetry = telemetry;
    }


    /*
    SERVO-LIKE MODE:
        -Updates target encoder position based on positionDelta,
            and makes sure it's not too far from the current encoder position.
        -Adjusts motor power based on PID calculation of difference between current encoder value and target encoder value.
    MOTOR-LIKE MODE:
        -Set motor directly to positionDelta (which should already be "eased")
    */
    public void loop(double positionDelta, boolean isServoMode)
    {
        if (isServoMode && !Double.isNaN(armMotor.getCurrentPosition())) //Control arm like continuous servo
        {
            //Update our target position
            currentEncoderTarget += positionDelta;
            //Make sure our new target isn't too far away from our current position
            double currentEncoderPosition = armMotor.getCurrentPosition();
            currentEncoderTarget = Range.clip(currentEncoderTarget, currentEncoderPosition - encoderTargetRange, currentEncoderPosition + encoderTargetRange);

            //Figure out motor power
            double motorPower = pidController.calculate(currentEncoderPosition, currentEncoderTarget);
            if (Double.isNaN(motorPower)) //Hopefully fixes weird competition issue
            {
                motorPower = 0;
            }
            motorPower = Range.clip(motorPower, -1, 1); //Make sure within acceptable range

            armMotor.setPower(motorPower);

            //Debug
            /*
            telemetry.addData("ArmController/positionDelta = ", positionDelta);
            telemetry.addData("ArmController/currentEncoderPositon = ", currentEncoderPosition);
            telemetry.addData("ArmController/currentEncoderTarget = ", currentEncoderTarget);
            telemetry.addData("ArmController/motorPower = ", motorPower);
            */
        }
        else //Just give power (input still "eased")
        {
            //Keep our two modes in sync
            currentEncoderTarget = armMotor.getCurrentPosition();

            //Update motor power
            double motorPower = Range.clip(positionDelta, -1, 1);
            armMotor.setPower(motorPower);
        }
    }
}


class AllClearGrabber
{
    Servo servo; //continuous

    public AllClearGrabber(Servo servo)
    {
        this.servo = servo;
    }

    public void loop(boolean isGoingUp, double goingDownFactor)
    {
        double servoPower = 0.5; //Base power to keep servo in place

        if (goingDownFactor > 0.3) //Going back
        {
            servoPower -= goingDownFactor / 2; //Negative power for going back
        }
        if (isGoingUp) //Going forward
        {
            servoPower += 0.5;
        }

        servoPower = Range.clip(servoPower, -1.0, 1.0);
        servo.setPosition(servoPower);
    }
}


//NOTE: one servo should already be reversed
class LeverHitter
{
    double max_pos;
    double min_pos;
    double start_pos;

    double currentPos; //will be initialized

    String readableName = "";

    //Refence should be passed already initialized and possibly reversed
    Servo servo;

    public LeverHitter(String readableName, Servo servo, double min_pos, double max_pos)
    {
        this.readableName = readableName;
        this.servo = servo;


        this.min_pos = min_pos;

        this.max_pos = max_pos;
        this.start_pos = max_pos;
        this.currentPos = max_pos;
    }

    //variableInput: positive is up, negative is down
    public void loop(double variableInput, boolean upOverride)
    {
        if (upOverride)
        {
            this.currentPos = this.start_pos;
        }

        this.currentPos += 0.005 * variableInput;
        this.currentPos = Range.clip(this.currentPos, this.min_pos, this.max_pos);

        this.servo.setPosition(currentPos);
    }

    public double getCurrentPos()
    {
        return this.currentPos;
    }
}