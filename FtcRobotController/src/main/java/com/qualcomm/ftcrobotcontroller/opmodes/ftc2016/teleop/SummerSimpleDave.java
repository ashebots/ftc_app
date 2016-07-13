package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.teleop;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class SummerSimpleDave extends OpMode
{
    DcMotor leftMotor;
    DcMotor rightMotor;
    IrSeekerSensor irSensor;


    @Override
    public void init()
    {
        leftMotor = hardwareMap.dcMotor.get("theBestMotorThatIsOnTheLeft");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor = hardwareMap.dcMotor.get("theBestMotorThatIsOnTheRight");

        irSensor = hardwareMap.irSeekerSensor.get("irSeeker");
    }


    private void turnLeft()
    {
        leftMotor.setPower(0);
        rightMotor.setPower(.5);
    }


    private void turnRight()
    {
        leftMotor.setPower(.5);
        rightMotor.setPower(0);
    }


    private void driveForward()
    {
        leftMotor.setPower(.5);
        rightMotor.setPower(.5);
    }


    @Override
    public void loop()
    {
        /*
        leftMotor.setPower(gamepad1.left_stick_y);
        rightMotor.setPower(gamepad1.right_stick_y);
        */

        if (irSensor.getAngle() > 0)
        {
            turnRight();
        }
        else if (irSensor.getAngle() < 0)
        {
            turnLeft();
        }
        else //we know it is exactly 0
        {
            driveForward();
        }
    }
}
