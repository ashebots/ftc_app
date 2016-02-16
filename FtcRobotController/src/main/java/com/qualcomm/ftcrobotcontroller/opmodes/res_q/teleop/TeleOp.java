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

    ArmController armController;

    //Both servos share position, 'cause one is software reversed
    //TODO: add actual positons
    /* USED FOR NON-CONTINUOUS SERVOS, SO NOT RIGHT NOW
    static double plowPosMax = 0.9; //upper position bound
    static double plowPosMin = 0.1; //lower position bound

    static double plowPosUp = 0.6;  //automatic go-to-pos up position
    static double plowPosDown = 0.4;//automatic go-to-pos down position

    double plowPosCurrent = plowPosUp; //initial position
    */


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

        armController = new ArmController(motorArm, new PIDSettings(0, 0, 0), telemetry);

        leverHitterLeft = new LeverHitter("Left", servoLeverHitterLeft);
        leverHitterRight = new LeverHitter("Right", servoLeverHitterRight);
    }




    @Override
    public void loop()
    {

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

        armController.loop(armDelta);

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

        leverHitterLeft.loop(gamepad2.left_stick_y * -1, gamepad2.left_bumper);
        leverHitterRight.loop(gamepad2.right_stick_y * -1, gamepad2.right_bumper);

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
    -Updates target encoder position based on positionDelta,
        and makes sure it's not too far from the current encoder position.
    -Adjusts motor power based on PID calculation of difference between current encoder value and target encoder value
     */
    public void loop(double positionDelta)
    {
        //Update our target position
        currentEncoderTarget += positionDelta;
        //Make sure our new target isn't too far away from our current position
        double currentEncoderPosition = armMotor.getCurrentPosition();
        currentEncoderTarget = Range.clip(currentEncoderTarget, currentEncoderPosition - encoderTargetRange, currentEncoderPosition + encoderTargetRange);

        //Figure out motor power
        double motorPower = pidController.calculate(currentEncoderPosition, currentEncoderTarget);
        motorPower = Range.clip(motorPower, -1, 1); //Make sure within acceptable range

        //DON'T DO THIS YET --> armMotor.setPower(motorPower);

        //Debug
        telemetry.addData("ArmController/positionDelta = ", positionDelta);
        telemetry.addData("ArmController/currentEncoderPositon = ", currentEncoderPosition);
        telemetry.addData("ArmController/currentEncoderTarget = ", currentEncoderTarget);
        telemetry.addData("ArmController/motorPower = ", motorPower);
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