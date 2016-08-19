package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;

//AdvOpMode adds a few new functions to the original OpMode.
public abstract class AdvOpMode extends OpMode {
    public double INF = Double.MAX_VALUE;
    //A faster way to generate a motor.
    public AdvMotor mtr(String name) {
        return new AdvMotor(hardwareMap.dcMotor.get(name));
    }
    //A faster way to generate a servo.
    public AdvServo srv(String name) {
        return new AdvServo(hardwareMap.servo.get(name));
    }
    //A faster way to generate an IMU Chassis.
    public IMUChassis imuchassis(String lName, String rName, String bName) {
        BNO055LIB bno = null;
        try {
            bno = new BNO055LIB(hardwareMap, bName
                    , (byte)(BNO055LIB.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    //addressing
                    , (byte)BNO055LIB.OPERATION_MODE_IMU);
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception: IMU UNREACHABLE, error message: "+e.getMessage() + ", localized: " +e.getLocalizedMessage());
        }
        return new IMUChassis(hardwareMap.dcMotor.get(lName),hardwareMap.dcMotor.get(rName),bno);
    }
    public Chassis chassis(String lName, String rName) {
        return new Chassis(hardwareMap.dcMotor.get(lName),hardwareMap.dcMotor.get(rName));
    }
    //This function automatically calibrates relative sensor values based on the state of a button.
    //It returns true when the part should activate.
    public boolean buttonPressed(HardwareComponent h, String s) {
        telemetry.addData("Status",s);
        if (s.equals("PRESSED")) {
            h.calibrate();
        } else if (s.equals("HELD")) {
            h.getValues();
            h.calibrate();
            return true;
        } else if (s.equals("RELEASED")) {
            h.stop();
        }
        return false;
    }
    public AdvUltrasonic sonic(String name, String legacy, int port) {
        return new AdvUltrasonic(hardwareMap.ultrasonicSensor.get(name),hardwareMap.legacyModule.get(legacy),port);
    }
}
