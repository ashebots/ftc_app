
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class CascadeEffectTeleop extends OpMode
{
    /*
     * Note that the servos should be configured so 0 means they're down or dumping,
     * and 1 means they're up.
     */
	public static double MIN_GOAL_GRABBER_POS = 0.0;
	public static double MAX_GOAL_GRABBER_POS = 1.0;

	public static double MIN_BASKET_DUMPER_POS = 0.0;
	public static double MAX_BASKET_DUMPER_POS = 1.0;

	DcMotor motorDriveLeft;
	DcMotor motorDriveRight;
	DcMotor motorWinch;

	Servo servoGoalGrabber;
	Servo servoBasketDumper;

	double goalGrabberPosition = 0.5;
	double goalGrabberDelta = 0.01;

	double basketDumperPosition = 0.5;
	double basketDumperDelta = 0.01;

	//Constructor. Called once upon object instantiation
	public CascadeEffectTeleop()
    {

	}

	//Called once when robot is enabled
	public void start()
	{
		//Initialize our hardware devices
		motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
		motorDriveLeft.setDirection(DcMotor.Direction.REVERSE); //Inverts the motor
		motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");

		motorWinch = hardwareMap.dcMotor.get("motorWinch");

		servoGoalGrabber = hardwareMap.servo.get("servoGoalGrabber");
		servoBasketDumper = hardwareMap.servo.get("servoBasketDumper");
	}

	//Called repeatedly every ~25 millisenconds
	public void loop()
	{
		//DRIVE
		//Basic arcade style driving. Uses the left joystick on controller 1.
		motorDriveLeft.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
		motorDriveRight.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);


		//WINCH UP AND DOWN
		//Basic control for moving the lifter/winch up and down using the right joystick on controller 1
		motorWinch.setPower(gamepad1.right_stick_y);


		//BASKET DUMPING
        //If the right bumper on controller 1 is pressed, try moving the basket up
        if (gamepad1.right_bumper == true)
        {
            basketDumperPosition += basketDumperDelta;
        }
        //If the right trigger (on controller 1) is pressed atleast a little bit, try moving the basket down proportionally
        if (gamepad1.right_trigger > 0.0)
        {
            basketDumperPosition -= (basketDumperDelta * gamepad1.right_trigger);
        }
        basketDumperPosition = Range.clip(basketDumperPosition, MIN_BASKET_DUMPER_POS, MAX_BASKET_DUMPER_POS);
        servoBasketDumper.setPosition(basketDumperPosition);


        //GOAL GRABBING
        //If the left bumper (on conroller 1) is pressed, try moving the basket up
        if (gamepad1.left_bumper == true)
        {
            goalGrabberPosition += goalGrabberDelta;
        }
        //If the left trigger (on controller 1) is pressed atleast a little bit, try moving the goal grabbing hook down proportionally
        if (gamepad1.left_trigger > 0.0)
        {
            goalGrabberPosition -= (goalGrabberDelta * gamepad1.left_trigger);
        }
        goalGrabberPosition = Range.clip(goalGrabberPosition, MIN_GOAL_GRABBER_POS, MAX_GOAL_GRABBER_POS);
        servoGoalGrabber.setPosition(goalGrabberPosition);


		//All telemetry data updates
		telemetry.addData("goalGrabberPosition", "Goal Grabber Position = " + goalGrabberPosition);
		telemetry.addData("basketDumperPosition", "Basket Dumper Position = " + basketDumperPosition);
	}

	//Called once when robot is disabled. Used for cleanup
	public void stop()
	{

	}
}
