package com.qualcomm.ftcrobotcontroller.opmodes.res_q.teleop_wheelie;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.ResQRobotBase;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.ashebots.ftcandroidlib.drive.ChassisArcade;
import org.ashebots.ftcandroidlib.easing.easetypes.CircEase;
import org.ashebots.ftcandroidlib.misc.Toggle;
import org.ashebots.ftcandroidlib.motor.Motor;


public class TeleOpWheelie extends ResQRobotBase
{
    ChassisArcade chassis;

    Toggle driveOrientationToggle;

    CircEase armPowerEaser;

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

        motorWheelieBar = new Motor(hardwareMap.dcMotor.get("wheelieMotor"));

        chassis = new ChassisArcade(motorDriveLeft, motorDriveRight);

        driveOrientationToggle = new Toggle();

        leverHitterLeft = new LeverHitter("Left", servoLeverHitterLeft);
        leverHitterRight = new LeverHitter("Right", servoLeverHitterRight);
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


        //region === ARM === (Collapse this w/ Android Studio)

        //TEMPORARY SERVO ARM
        //servoArm.setPosition(0.5 + (gamepad1.right_stick_y / 6) * -1); //temporary servo

        //TODO: ADD LIMITS
       /* double armPower = armPowerEaser.easeIn(Math.abs(gamepad1.right_stick_y), 0.0, 1.0, 1.0);
        if (gamepad1.right_stick_y * -1 < 0)
        {
            armPower *= -1;
        }
        motorArm.setPower(armPower); //CURRENTLY DISABLED BECAUSE MECHANICAL
*/
        motorArm.setPower(gamepad1.right_stick_y * -1);


        //endregion

        //region === WHEELIE BAR

        if (gamepad1.right_bumper)
        {
            motorWheelieBar.setPower(0.6);
        }
        else if (gamepad1.right_trigger > 0.5)
        {
            motorWheelieBar.setPower(-0.6);
        }
        else
        {
            motorWheelieBar.setPower(0);
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

        telemetry.addData("5: Left drive encoder = ", motorDriveLeft.getCurrentPosition());
        telemetry.addData("6: Right drive encoder = ", motorDriveRight.getCurrentPosition());
        //endregion

    }
}

//NOTE: one should already be reversed
class LeverHitter
{
    static double MAX_POS = 1.0;
    static double MIN_POS = 0.0;
    static double START_POS = 0.9;

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