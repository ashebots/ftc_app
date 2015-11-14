package com.qualcomm.ftcrobotcontroller.opmodes.res_q;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

public abstract class ResQRobot extends OpMode
{
    DcMotor motorDriveLeft;
    DcMotor motorDriveRight;

    DcMotor motorArmJoint1; //First arm joint, one closest to robot
    DcMotor motorArmJoint2; //Second arm join, one farthest from robot
    DcMotor motorArmSwivel; //Motor that turns the arm's lazy-susan base

    DcMotor motorBoxSweeper;

    TouchSensor sensorTouchArmJoint1; //Sensor to detect when the first arm join is fully closed
    TouchSensor sensorTouchArmJoint2; //Sensor to detect when the second arm join is fully closed


    public void init()
    {
        motorDriveLeft = hardwareMap.dcMotor.get("motorDriveLeft");
        motorDriveRight = hardwareMap.dcMotor.get("motorDriveRight");
        motorDriveRight.setDirection(DcMotor.Direction.REVERSE);

        motorArmJoint1 = hardwareMap.dcMotor.get("motorArmJoint1");
        motorArmJoint1.setDirection(DcMotor.Direction.REVERSE);
        motorArmJoint2 = hardwareMap.dcMotor.get("motorArmJoint2");
        motorArmJoint2.setDirection(DcMotor.Direction.REVERSE);
        motorArmSwivel = hardwareMap.dcMotor.get("motorArmSwivel");

        motorBoxSweeper = hardwareMap.dcMotor.get("motorBoxSweeper");

        sensorTouchArmJoint1 = hardwareMap.touchSensor.get("sensorTouchArmJoint1");
        sensorTouchArmJoint2 = hardwareMap.touchSensor.get("sensorTouchArmJoint2");
    }
}
