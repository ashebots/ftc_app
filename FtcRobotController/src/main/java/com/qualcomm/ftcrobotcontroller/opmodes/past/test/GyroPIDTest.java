package com.qualcomm.ftcrobotcontroller.opmodes.past.test;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;

import com.qualcomm.ftcrobotcontroller.opmodes.past.lib.AdafruitIMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.ashebots.ftcandroidlib.control.PIDController;
import org.ashebots.ftcandroidlib.control.PIDSettings;
import org.ashebots.ftcandroidlib.motor.Motor;


public class GyroPIDTest extends OpMode {

    double targetHeading = 0.0;

    Motor motorDriveLeft;
    Motor motorDriveRight;

    PIDSettings headingPIDSettings;
    PIDController headingPIDController;

    AdafruitIMU boschBNO055;

    //The following arrays contain both the Euler angles reported by the IMU (indices = 0) AND the
    // Tait-Bryan angles calculated from the 4 components of the quaternion vector (indices = 1)
    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];

    long systemTime;//Relevant values of System.nanoTime

    /************************************************************************************************
     * The following method was introduced in the 3 August 2015 FTC SDK beta release and it runs
     * before "start" runs.
     */
    @Override
    public void init() {
        systemTime = System.nanoTime();
        try {
            boschBNO055 = new AdafruitIMU(hardwareMap, "bno055"

                    //The following was required when the definition of the "I2cDevice" class was incomplete.
                    //, "cdim", 5

                    , (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    //addressing
                    , (byte)AdafruitIMU.OPERATION_MODE_IMU);
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception: " + e.getMessage());
        }
        Log.i("FtcRobotController", "IMU Init method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");
        //ADDRESS_B is the "standard" I2C bus address for the Bosch BNO055 (IMU data sheet, p. 90).
        //BUT DAVID PIERCE, MENTOR OF TEAM 8886, HAS EXAMINED THE SCHEMATIC FOR THE ADAFRUIT BOARD ON
        //WHICH THE IMU CHIP IS MOUNTED. SINCE THE SCHEMATIC SHOWS THAT THE COM3 PIN IS PULLED LOW,
        //ADDRESS_A IS THE IMU'S OPERATIVE I2C BUS ADDRESS
        //IMU is an appropriate operational mode for FTC competitions. (See the IMU datasheet, Table
        // 3-3, p.20 and Table 3-5, p.21.)
    }

    /************************************************************************************************
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void start() {
        systemTime = System.nanoTime();
        boschBNO055.startIMU();//Set up the IMU as needed for a continual stream of I2C reads.
        Log.i("FtcRobotController", "IMU Start method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");


        motorDriveLeft = new Motor(hardwareMap.dcMotor.get("left"));
        motorDriveLeft.setDirection(DcMotor.Direction.REVERSE);
        motorDriveRight = new Motor(hardwareMap.dcMotor.get("right"));

        headingPIDSettings = new PIDSettings(0.02, 0, 0);
        headingPIDController = new PIDController(headingPIDSettings);
    }

    //Yeah this is gross, whatcha gon' do about it? Prototyping, ya'll...
    @Override
    public void loop() {
        //Configure P
        if (gamepad1.dpad_up)
            headingPIDSettings.setProportionalTerm(headingPIDSettings.getProportionalTerm() + 0.00001);
        if (gamepad1.dpad_up)
            headingPIDSettings.setProportionalTerm(headingPIDSettings.getProportionalTerm() - 0.00001);

        //Configure I
        if (gamepad1.left_bumper)
            headingPIDSettings.setIntegralTerm(headingPIDSettings.getIntegralTerm() + 0.00001);
        if (gamepad1.left_trigger > .2)
            headingPIDSettings.setIntegralTerm(headingPIDSettings.getIntegralTerm() - 0.00001);

        //Configure D
        if (gamepad1.right_bumper)
            headingPIDSettings.setDerivativeTerm(headingPIDSettings.getDerivativeTerm() + 0.00001);
        if (gamepad1.right_trigger > .2)
            headingPIDSettings.setDerivativeTerm(headingPIDSettings.getDerivativeTerm() - 0.00001);


        boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);

        telemetry.addData("Headings(yaw): ",
                String.format("Euler= %4.5f", yawAngle[0]));


        double pidValue = headingPIDController.calculate(yawAngle[0], targetHeading);
        double turnPower = Range.clip(pidValue, -1.0, 1.0);

        telemetry.addData("PID value = ", pidValue);
        telemetry.addData("Turn power = ", turnPower);

        telemetry.addData("Z: PID settings = {",
                        headingPIDSettings.getProportionalTerm() + ", "
                        + headingPIDSettings.getIntegralTerm() + ", "
                        + headingPIDSettings.getProportionalTerm() + "}");

        motorDriveLeft.setPower(-turnPower);
        motorDriveRight.setPower(turnPower);
    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {
        //When the FTC Driver Station's "Start with Timer" button commands autonomous mode to start,
        //then stop after 30 seconds, stop the motors immediately!
        //Following this method, the underlying FTC system will call a "stop" routine of its own
        systemTime = System.nanoTime();
        Log.i("FtcRobotController", "IMU Stop method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");
    }
}