package com.qualcomm.ftcrobotcontroller.opmodes.past.auto_basket.components;


import com.qualcomm.ftcrobotcontroller.opmodes.past.lib.IMU;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.control.PIDController;
import org.ashebots.ftcandroidlib.control.PIDSettings;
import org.ashebots.ftcandroidlib.motor.Motor;

public class AutoDriveController
{
    IMU imu;

    Motor motorDriveLeft;
    Motor motorDriveRight;

    PIDController headingPIDController;
    PIDController distancePIDController;

    Telemetry telemetry;


    //revolution of motor shaft, not treads
    int ticksPerRevolution;
    double distancePerRevolution = 7.95; //Inches that the robot moves per shaft revolution.


    int targetEncoderTicks = 0;
    double requestedDrivePower = 1;

    double targetHeading = 0;


    public AutoDriveController(Motor motorDriveLeft, Motor motorDriveRight, IMU imu, PIDSettings headingPIDSettings, PIDSettings distancePIDSettings, Telemetry telemetry)
    {
        this.motorDriveLeft = motorDriveLeft;
        this.motorDriveRight = motorDriveRight;

        this.imu = imu;

        this.headingPIDController = new PIDController(headingPIDSettings);
        this.distancePIDController = new PIDController(distancePIDSettings);

        this.telemetry = telemetry;


        ticksPerRevolution = motorDriveLeft.getEncoderTicksPerRevolution(); //assume that motors are the same
    }


    public void start()
    {
        targetHeading = imu.getContinuousYaw();

        motorDriveLeft.setCurrentPosition(0);
        motorDriveRight.setCurrentPosition(0);
    }


    public void setHeadingRelative(double degrees)
    {
        targetHeading += degrees;
    }


    //Inches; positive distance forward, negative distance back; power always positive
    public void setDriveDistanceRelative(double distance, double power)
    {
        power = Math.abs(power);
        power = Range.clip(power, 0, 1.0);
        requestedDrivePower = power;

        motorDriveLeft.setCurrentPosition(0);
        motorDriveRight.setCurrentPosition(0);

        targetEncoderTicks = calcEncoderTicksForDistance(distance);
    }
    public void setDriveDistanceRelative(double distance) { setDriveDistanceRelative(distance, 1.0); }



    //MUST BE CALLED IN LOOP()
    public void updatePosition()
    {
        //First calculate distance error. This will serve as our primary driving force
        double distanceError = distancePIDController.calculate(getCurrentEncoderTicks(), targetEncoderTicks);
        if (Double.isNaN(distanceError)) {
            distanceError = 0;
        }
        distanceError = Range.clip(distanceError, -1.0, 1.0);
        distanceError = Range.scale(distanceError, -1.0, 1.0, -requestedDrivePower, requestedDrivePower);

        //Calculate heading error. This will serve as a weak driving force, when maintaining a heading,
        //and a strong and entirely overpowering force when getting to a new heading. Notice that it is NOT clipped YET
        double headingError = headingPIDController.calculate(imu.getContinuousYaw(), targetHeading);
        if (Double.isNaN(headingError)) {
            headingError = 0;
            telemetry.addData("WARNING", "HEADING ERROR IS NaN");
        }

        //Add forces together
        double leftDrivePower = distanceError + headingError;
        double rightDrivePower = distanceError - headingError;

        //Once added, clip again.
        //Note that if distanceError is at max, then only the negative side of headingError will
        //affect the output, so it might work half as slow... :(
        leftDrivePower = Range.clip(leftDrivePower, -1.0, 1.0);
        rightDrivePower = Range.clip(rightDrivePower, -1.0, 1.0);

        motorDriveLeft.setPower(leftDrivePower);
        motorDriveRight.setPower(rightDrivePower);


        telemetry.addData("requestedDrivePower = ", requestedDrivePower);
        telemetry.addData("headingError = ", headingError);
        telemetry.addData("distanceError = ", distanceError);
        //telemetry.addData("targetHeading", targetHeading);
        //telemetry.addData("currentHeading", imu.getContinuousYaw());
        //telemetry.addData("headingError", headingError);
        //telemetry.addData("current ticks", getCurrentEncoderTicks());
        //telemetry.addData("target ticks", targetEncoderTicks);
        //telemetry.addData("distanceError", distanceError);
    }



    public double distanceFromTargetDistance()
    {
        int encoderTicksAway = Math.abs(targetEncoderTicks - getCurrentEncoderTicks());
        return calcDistanceForEncoderTicks(encoderTicksAway);
    }

    //Absolute value. Could be used to decide when to proceed after turning
    public double degreesFromTargetHeading()
    {
        return Math.abs(targetHeading - imu.getContinuousYaw());
    }



    private int calcEncoderTicksForDistance(double distance)
    {
        double revolutionsToTarget = distance / distancePerRevolution;
        return (int) (revolutionsToTarget * (double) ticksPerRevolution);
    }

    private double calcDistanceForEncoderTicks(int ticks)
    {
        double revolutions = ticks / ticksPerRevolution;
        return revolutions * distancePerRevolution;
    }

    //Average of motor.getCurrentPosition()
    private int getCurrentEncoderTicks()
    {
        return ( motorDriveLeft.getCurrentPosition() + motorDriveRight.getCurrentPosition() ) / 2;
    }
}
