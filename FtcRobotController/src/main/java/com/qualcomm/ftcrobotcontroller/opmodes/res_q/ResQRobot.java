package com.qualcomm.ftcrobotcontroller.opmodes.res_q;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.ashebots.ftcandroidlib.motor.Motor;

public abstract class ResQRobot extends OpMode
{
    Motor motorDriveLeft;
    Motor motorDriveRight;

    Motor motorArmJoint1; //First arm joint, one closest to robot
    Motor motorArmJoint2; //Second arm join, one farthest from robot
    Motor motorArmSwivel; //Motor that turns the arm's lazy-susan base

    Motor motorBoxSweeper;

    TouchSensor sensorTouchArmJoint1; //Sensor to detect when the first arm join is fully closed
    TouchSensor sensorTouchArmJoint2; //Sensor to detect when the second arm join is fully closed


    @Override
    public void init()
    {
        motorDriveLeft = new Motor(hardwareMap.dcMotor.get("motorDriveLeft"));
        motorDriveRight = new Motor(hardwareMap.dcMotor.get("motorDriveRight"));
        motorDriveRight.setDirection(DcMotor.Direction.REVERSE);

        motorArmJoint1 = new Motor(hardwareMap.dcMotor.get("motorArmJoint1"));
        motorArmJoint1.setDirection(DcMotor.Direction.REVERSE);
        motorArmJoint2 = new Motor(hardwareMap.dcMotor.get("motorArmJoint2"));
        motorArmJoint2.setDirection(DcMotor.Direction.REVERSE);
        motorArmSwivel = new Motor(hardwareMap.dcMotor.get("motorArmSwivel"));

        motorBoxSweeper = new Motor(hardwareMap.dcMotor.get("motorBoxSweeper"));

        sensorTouchArmJoint1 = hardwareMap.touchSensor.get("sensorTouchArmJoint1");
        sensorTouchArmJoint2 = hardwareMap.touchSensor.get("sensorTouchArmJoint2");
    }
}
