package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket;


import android.util.Log;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.components.AutoDriveController;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib.AdafruitIMU;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib.IMU;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.lib.ImperialRoboticsBNO055;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.shared.ResQRobotBase;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.ashebots.ftcandroidlib.control.PIDSettings;

public abstract class AutoBasketBase extends ResQRobotBase
{
    //=== PROGRAM STATE CONTROL ===

    //General overall current "stage" of the program
    enum ProgramState
    {
        POTENTIALLY_DELAY,
        DRIVE_AWAY_FROM_WALL,
        ROTATE_TOWARDS_BEACON,
        DRIVE_PAST_BEACON,
        DRIVE_BACK_TO_BEACON,
        ROTATE_TOWARDS_BEACON_AGAIN,
        DRIVE_TO_BEACON,
        DUMP_CLIMBERS,
        END
    }
    ProgramState programState = ProgramState.POTENTIALLY_DELAY;

    //programState agnostic, used in each individual programState case for more control
    int subState = 0;

    //All turn angles are multiplied by this, to invert them on one of the alliances
    public int allianceRotationFactor = 1; //Will be set by red and blue subclasses; BLUE is POSITIVE, RED is NEGATIVE.

    //Seconds to wait before starting program. Set to a value in sub programs
    public double startDelaySeconds = 0;

    //Used as a time-based fallback for actions completing //TODO: WRITE A BETTER GODDAMN DESCRIPTION
    ElapsedTime currentActionTimer;



    //=== AUTONOMOUS SPECIFIC SUB SYSTEMS ===

    public AutoDriveController driveController;

    public PIDSettings headingPIDSettings;
    public PIDSettings distancePIDSettings;


    public IMU imu;

    @Override
    public void init()
    {
        //Initialize shared components/hardware
        super.init();


        //IMU
        try {
            imu = new ImperialRoboticsBNO055(new AdafruitIMU(hardwareMap, "bno055"
                    , (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    //addressing
                    , (byte)AdafruitIMU.OPERATION_MODE_IMU));
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception instantiating IMU: " + e.getMessage());
        }


        //Initialize other autonomous specifics
        headingPIDSettings = new PIDSettings(0.055, 0, 0.002);
        distancePIDSettings = new PIDSettings(0.004, 0, 1E-4);
        driveController = new AutoDriveController(motorDriveLeft, motorDriveRight, imu, headingPIDSettings, distancePIDSettings, telemetry);

        currentActionTimer = new ElapsedTime();
    }

    @Override
    public void start()
    {
        super.start();
        //Set up IMU

        imu.start();
        imu.invertYaw(true);

        //Reset getRuntime() which is used to stop program after 30 sec
        resetStartTime();

        driveController.start();

        //Makes sure servo arms are up and powered
        stopMotorsAndServos();
    }

    @Override
    public void loop()
    {
        //Gracefully stop program if time has run out.
        if (getRuntime() >= 30)
        {
            requestOpModeStop();
            //telemetry.addData("STATUS = ", "OUT OF TIME");
        }

        //Refresh IMU data
        imu.updateAngles();

        //Update motor powers to achieve target heading/position
        driveController.updatePosition();

        //Update debug info. Should this be disabled for competition?
        updateTelemetry();


        //Do actions based on greater programState and lesser subState
        switch (programState)
        {
            //Will delay program if startDelaySeconds has been set (ideally to AutoBasketStartDelay.getTimeDelay())
            case POTENTIALLY_DELAY:
                switch (subState)
                {
                    case 0:
                        currentActionTimer.reset();
                        subState++;
                        break;

                    case 1:
                        if (currentActionTimer.seconds() >= startDelaySeconds)
                        {
                            goToNextProgramState();
                        }
                        break;
                }
                break;

            case DRIVE_AWAY_FROM_WALL:
                switch (subState)
                {
                    case 0:
                        driveController.setDriveDistanceRelative(18); //Drive 6 inches away from wall so we can turn
                        currentActionTimer.reset();
                        subState++; //Go to next subState
                        break;

                    case 1:
                        //Wait until arrived within 0.5 inches of target, or too much time elapses
                        if (driveController.distanceFromTargetDistance() < 0.5 || currentActionTimer.seconds() > 3)
                        {
                            goToNextProgramState();
                        }
                        break;
                }
                break;

            case ROTATE_TOWARDS_BEACON:
                switch (subState)
                {
                    case 0:
                        driveController.setHeadingRelative(45 * allianceRotationFactor);
                        currentActionTimer.reset();
                        subState++;
                        break;

                    case 1:
                        //Wait until arrived within 1.5 degrees (out of 360) of target heading, or too much time elapses
                        if (driveController.degreesFromTargetHeading() < 1.0 || currentActionTimer.seconds() > 3)
                        {
                            goToNextProgramState();
                        }
                        break;
                }
                break;

            case DRIVE_PAST_BEACON:
                switch (subState)
                {
                    case 0:
                        driveController.setDriveDistanceRelative(104, 0.5); //Made up value
                        currentActionTimer.reset();
                        subState++;
                        break;

                    case 1:
                        //Wait until arrived within 0.5 inches of target, or too much time elapses
                        if (driveController.distanceFromTargetDistance() < 0.5 || currentActionTimer.seconds() > 16) //TODO: CHANGE FALLBACK TIME
                        {
                            goToNextProgramState();
                        }
                        break;
                }
                break;

            case DRIVE_BACK_TO_BEACON:
                switch (subState)
                {
                    case 0:
                        driveController.setDriveDistanceRelative(-26);
                        currentActionTimer.reset();
                        subState++;
                        break;

                    case 1:
                        if (driveController.distanceFromTargetDistance() < 0.3 || currentActionTimer.seconds() > 4) //TODO: CHANGE FALLBACK TIME
                        {
                            goToNextProgramState();
                        }
                        break;
                }
                break;

            case ROTATE_TOWARDS_BEACON_AGAIN:
                switch (subState)
                {
                    case 0:
                        driveController.setHeadingRelative(45 * allianceRotationFactor);
                        currentActionTimer.reset();
                        subState++;
                        break;

                    case 1:
                        //Wait until arrived within 1.5 degrees (out of 360) of target heading, or too much time elapses
                        if (driveController.degreesFromTargetHeading() < 0.5 || currentActionTimer.seconds() > 3)
                        {
                            goToNextProgramState();
                        }
                        break;
                }
                break;

            case DRIVE_TO_BEACON:
                switch (subState)
                {
                    case 0:
                        //Start driving
                        driveController.setDriveDistanceRelative(27); //Made up value

                        //Start plow moving up
                        //servoPlow.setPosition(0.8); //continuous servo

                        //Move to next state
                        currentActionTimer.reset();
                        subState++;
                        break;

                    case 1:
                        //Stop plow moving up, before going on to next programState
                        if (currentActionTimer.seconds() > 2.5)
                        {
                            servoPlow.setPosition(0.5);
                        }

                        //Wait until arrived within x inches of target, or too much time elapses
                        if (driveController.distanceFromTargetDistance() < 0.1 || currentActionTimer.seconds() > 5)
                        {
                            //Make sure to stop plow just in case we got to destination early
                            servoPlow.setPosition(0.5);

                            //Move on
                            goToNextProgramState();
                        }
                        break;
                }
                break;

            case DUMP_CLIMBERS:
                switch (subState)
                {
                    //Start dumping
                    case 0:
                        servoClimberDumper.setPosition(0.7); //Continuous servo
                        currentActionTimer.reset();
                        subState++;
                        break;
                    //Wait for dumping to complete
                    case 1:
                        if (currentActionTimer.seconds() > 3)
                        {
                            subState++;
                        }
                        break;
                    //Start returning
                    case 2:
                        servoClimberDumper.setPosition(0.3); //Continuous servo
                        currentActionTimer.reset();
                        subState++;
                        break;
                    //Wait for returning to complete, then stop
                    case 3:
                        if (currentActionTimer.seconds() > 3)
                        {
                            goToNextProgramState();
                        }
                        break;
                }
                break;

            case END:
                requestOpModeStop();
                break;

            default:
                requestOpModeStop();
                break;
        }

    }

    @Override
    public void stop()
    {
        super.stop();
    }


    void goToNextProgramState()
    {
        //Get next value in ProgramState enum. Should go to default switch() case after enum ends.
        ProgramState[] possibleStates = ProgramState.values();
        int nextStateIndex = (programState.ordinal() + 1) % possibleStates.length;
        programState = possibleStates[nextStateIndex];

        //Reset subState
        subState = 0;

        //DEBUG
        //subState = -1; //TODO: REMOVE THIS, SET TO 0 INSTEAD
    }


    void updateTelemetry()
    {
        /*
        telemetry.addData("1: programState = ", programState);
        telemetry.addData("2: programState ordinal = ", programState.ordinal());
        telemetry.addData("3: subState = ", subState);

        telemetry.addData("4: currentActionTimer = ", currentActionTimer.seconds());

        telemetry.addData("5: Distance Error = ", driveController.distanceFromTargetDistance());
        */
        telemetry.addData("1: Degree Error = ", driveController.degreesFromTargetHeading());

        telemetry.addData("2: Run time = ", getRuntime());



        //telemetry.addData(":  = ", );
    }
}
