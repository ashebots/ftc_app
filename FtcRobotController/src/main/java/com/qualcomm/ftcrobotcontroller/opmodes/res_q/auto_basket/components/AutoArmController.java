package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.components;

import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.control.PIDController;
import org.ashebots.ftcandroidlib.control.PIDSettings;
import org.ashebots.ftcandroidlib.motor.Motor;

/**
 * Created by Nolan on 3/21/2016.
 */

public class AutoArmController
{
    static final int MAX_DEGREES = 120; //TODO: figure out real value //This the maximum safe value that the arm can go to (relative to start) (while the robot is still) without flipping

    static final int INPUT_GEAR_TEETH = 8; //TODO: value
    static final int OUTPUT_GEAR_TEETH = 40; //TODO value
    //static final double GEAR_RATIO = OUTPUT_GEAR_TEETH / (double) INPUT_GEAR_TEETH;

    int encoderTicksPerArmRev; //How many encoder ticks the motor would have to turn for the ARM to revolve once. (Set

    int currentEncoderTarget = 0;
    double targetDegrees = 0;

    Telemetry telemetry;
    Motor motorArm;
    PIDController pidController;


    //For init button.
    public AutoArmController(Motor motorArm, PIDSettings pidSettings, Telemetry telemetry)
    {
        this.motorArm = motorArm;
        this.pidController = new PIDController(pidSettings);
        this.telemetry = telemetry;

        encoderTicksPerArmRev = motorArm.getEncoderTicksPerRevolution() * (OUTPUT_GEAR_TEETH / INPUT_GEAR_TEETH);
    }

    //For start button. Arm assumed to be in resting position
    public void start()
    {
        motorArm.setCurrentPosition(0);
        currentEncoderTarget = 0;
    }



    //Will clamp target into acceptable bounds. (0 to MAX_DEGREES)
    public void setTargetPositionDegrees(double degrees)
    {
        degrees = Range.clip(degrees, 0, MAX_DEGREES);
        currentEncoderTarget = degreesToEncoderTicks(degrees);

        targetDegrees = degrees; //Not used in calculations, but stored for user's reference
    }
    public double getTargetPositionDegrees() { return targetDegrees; }


    //Must be called in loop. Updates motor power based on current position and target position
    public void updatePosition()
    {
        telemetry.addData("arm encoder target = ", currentEncoderTarget);
        telemetry.addData("arm encoder position = ", motorArm.getCurrentPosition());

        double motorPower = pidController.calculate((double) motorArm.getCurrentPosition(), (double) currentEncoderTarget);
        telemetry.addData("motorArm raw power = ", motorPower);

        motorPower = Range.clip(motorPower, -1.0, 1.0);

        telemetry.addData("motorArm power = ", motorPower);
        motorArm.setPower(motorPower);
    }


    //Calculates encoder position that corresponds to input degrees
    int degreesToEncoderTicks(double degrees) //Degrees should be 0 - 359. Will be clamped.
    {
        degrees = Range.clip(degrees, 0, 359); //Note: this should ideally be 0 to 359.9999999999... but it doesn't matter
        return (int) (degrees / 360 * encoderTicksPerArmRev);
    }
}
