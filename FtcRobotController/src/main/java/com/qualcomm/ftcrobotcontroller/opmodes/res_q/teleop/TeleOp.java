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

    Toggle driveOrientationToggle;

    AllianceColor ourAlliance = AllianceColor.UNKNOWN; //Enum can be BLUE or RED


    boolean plowIsDown = false; //true is down ready to push, false is up and out of way

    //TODO: add actual defaults
    double leverHitterPosL = 0.0;
    double leverHitterPosR = 0.0;

    @Override
    public void init()
    {
        //This will call the init() method in the parent "ResQRobot" class. That is the class
        //where we store and setup all our HARDWARE variables.
        super.init();

        chassis = new ChassisArcade(motorDriveLeft, motorDriveRight);

        driveOrientationToggle = new Toggle();
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

        //TODO: ADD LIMITS
        motorArm.setPower(gamepad1.right_stick_y * -1);

        //endregion

        //region === PLOW ===
        /*
        //Toggle target state for plow
        if (gamepad1.right_trigger > 0.3)
        {
            plowIsDown = true;
        }
        else if (gamepad1.right_bumper)
        {
            plowIsDown = false;
        }

        //Make sure plow is in target pos, based on state
        if (plowIsDown)
        {

        }
        else //plow up (plowIsDown == false)
        {

        }
        */


        //endregion



        //region === LEVER HITTER SERVOS ===

        /*
        //LEFT
        leverHitterPosL += 0.005 * gamepad2.left_stick_y;

        leverHitterPosL = Range.clip(leverHitterPosL, 0.0, 1.0);
        servoLeverHitterLeft.setPosition(leverHitterPosL);

        telemetry.addData("left lever hitter target pos", leverHitterPosL);
        telemetry.addData("left lever hitter pos = ", servoLeverHitterLeft.getPosition());

        //RIGHT
        leverHitterPosR += 0.005 * gamepad2.right_stick_y;

        leverHitterPosR = Range.clip(leverHitterPosR, 0.0, 1.0);
        servoLeverHitterRight.setPosition(leverHitterPosR);

        telemetry.addData("right lever hitter target pos", leverHitterPosR);
        telemetry.addData("right lever hitter pos = ", servoLeverHitterRight.getPosition());

        //endregion


        //Quick little hack to tune the PID controller for the arm swivel
        //tunePID(armSwivelHeadingSettings);

        //region === TELEMETRY === (Collapse this w/ Android Studio)

        telemetry.addData("_Alliance = ", ourAlliance);
        //telemetry.addData("1: Left drive encoder = ", motorDriveLeft.getCurrentPosition());
        //telemetry.addData("2: Right drive encoder = ", motorDriveRight.getCurrentPosition());
        //endregion
        */
    }
}