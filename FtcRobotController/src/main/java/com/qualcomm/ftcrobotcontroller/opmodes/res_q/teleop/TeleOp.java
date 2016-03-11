package com.qualcomm.ftcrobotcontroller.opmodes.res_q.teleop;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.ResQRobotBase;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.control.PIDController;
import org.ashebots.ftcandroidlib.control.PIDSettings;
import org.ashebots.ftcandroidlib.drive.ChassisArcade;
import org.ashebots.ftcandroidlib.easing.easetypes.CircEase;
import org.ashebots.ftcandroidlib.misc.Toggle;
import org.ashebots.ftcandroidlib.motor.Motor;


public class TeleOp extends ResQRobotBase
{
    ChassisArcade chassis;

    Toggle driveOrientationToggle;

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

        armPIDSettings = new PIDSettings(0.001, 0, 0);
        armController = new ArmController(motorArm, armPIDSettings, telemetry);

        leftAllClearGrabber = new AllClearGrabber(servoAllClearLeft);
        rightAllClearGrabber = new AllClearGrabber(servoAllClearRight);

        leverHitterLeft = new LeverHitter("Left", servoLeverHitterLeft);
        leverHitterRight = new LeverHitter("Right", servoLeverHitterRight);
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
        chassis.Drive(driveX, driveY);
        telemetry.addData("Left motor power = ", motorDriveLeft.getPower());
        telemetry.addData("Right motor power = ", motorDriveRight.getPower());

        //endregion


        //region === ARM === (Collapse this w/ Android Studio)

        double armInput = gamepad1.right_stick_y * -1; //Get joystick input and correct direction
        double armDelta = easeInCirc(Math.abs(armInput), 0, 1, 1); //Get an (absolute) "eased" value from 0 to 1. It rises slowly at first then rises steeply at the end
        armDelta *= Math.signum(armInput); //signum = 1 if input positive, -1 if negative, 0 if zero. This turns back to negative if input negative.

        armController.loop(armDelta, gamepad1.left_bumper);

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


        //region === ALL CLEAR GRABBERS

        //LEFT all clear
        leftAllClearGrabber.loop(gamepad2.left_bumper, gamepad2.left_trigger);
        //RIGHT all clear
        rightAllClearGrabber.loop(gamepad2.right_bumper, gamepad2.right_trigger);

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


    void tunePID(PIDSettings pidSettings)
    {
        //pTerm
        if (gamepad2.dpad_up)
        {
            pidSettings.setProportionalTerm(pidSettings.getProportionalTerm() + 0.00001);
        }
        else if (gamepad2.dpad_down)
        {
            pidSettings.setProportionalTerm(pidSettings.getProportionalTerm() - 0.00001);
        }

        //iTerm
        if (gamepad2.left_stick_y < -0.3)
        {
            pidSettings.setIntegralTerm(pidSettings.getIntegralTerm() + 0.00001);
        }
        else if (gamepad2.left_stick_y > 0.3)
        {
            pidSettings.setIntegralTerm(pidSettings.getIntegralTerm() - 0.00001);
        }

        //iTerm
        if (gamepad2.right_stick_y < -0.3)
        {
            pidSettings.setDerivativeTerm(pidSettings.getDerivativeTerm() + 0.00001);
        }
        else if (gamepad2.right_stick_y > 0.3)
        {
            pidSettings.setDerivativeTerm(pidSettings.getDerivativeTerm() - 0.00001);
        }

        telemetry.addData("1: pTerm=", pidSettings.getProportionalTerm());
        telemetry.addData("2: iTerm=", pidSettings.getIntegralTerm());
        telemetry.addData("3: dTerm=", pidSettings.getDerivativeTerm());
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


//NOTE: one motor should already be reversed
class LeverHitter
{
    static double MAX_POS = 1.0;
    static double MIN_POS = 0.0;
    static double START_POS = 0.99;

    double currentPos; //will be initialized

    String readableName = "";

    //Refence should be passed already initialized and possibly reversed
    Servo servo;

    public LeverHitter(String readableName, Servo servo)
    {
        this.readableName = readableName;
        this.servo = servo;

        this.currentPos = LeverHitter.START_POS;
    }

    //variableInput: positive is up, negative is down
    public void loop(double variableInput, boolean upOverride)
    {
        if (upOverride)
        {
            this.currentPos = LeverHitter.START_POS;
        }

        this.currentPos += 0.005 * variableInput;
        this.currentPos = Range.clip(this.currentPos, LeverHitter.MIN_POS, LeverHitter.MAX_POS);

        this.servo.setPosition(currentPos);
    }

    public double getCurrentPos()
    {
        return this.currentPos;
    }
}