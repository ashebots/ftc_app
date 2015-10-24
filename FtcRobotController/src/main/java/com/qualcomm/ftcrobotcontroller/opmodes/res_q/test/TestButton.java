//This is an autonomous program for driving to and pushing the correct button.

package com.qualcomm.ftcrobotcontroller.opmodes.res_q.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

public class TestButton extends LinearOpMode
{
    //define hardware
    DcMotor right;
    DcMotor left;
    Servo push;
    double firstpower = 0.5;
    double secondpower = 0.5;
    double thirdpower = 0.5;
    double fourthpower = 0.5;

    int firstdistance = 3000;
    int seconddistanceleft = 2000;
    int seconddistanceright = 1000;
    int thirddistance = 500;
    int fourthdistance = 500;

    double ttpower = secondpower * seconddistanceleft / seconddistanceright;

    @Override
    public void runOpMode() throws InterruptedException
    {
        //set up hardware
        left = hardwareMap.dcMotor.get("left");
        left.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        right = hardwareMap.dcMotor.get("right");
        right.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        push = hardwareMap.servo.get("push");

        left.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        //run program

        double leftpwr = firstpower;
        double rightpwr = firstpower;
        int rrotation = 0;
        int lrotation = 0;
        int truer = 0;
        int truel = 0;

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation > firstdistance) {
                leftpwr = 0;
            }
            if (rrotation > firstdistance) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation += (right.getCurrentPosition() - truer);
            lrotation += (left.getCurrentPosition() - truel);
            truel = left.getCurrentPosition();
            truer = right.getCurrentPosition();

            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);

            waitOneFullHardwareCycle();
        }

        leftpwr = secondpower;
        rightpwr = ttpower;
        rrotation = 0;
        lrotation = 0;

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation > seconddistanceleft) {
                leftpwr = 0;
            }
            if (rrotation > seconddistanceright) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation += (right.getCurrentPosition() - truer);
            lrotation += (left.getCurrentPosition() - truel);
            truel = left.getCurrentPosition();
            truer = right.getCurrentPosition();

            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);

            waitOneFullHardwareCycle();
        }

        leftpwr = thirdpower;
        rightpwr = - thirdpower;
        rrotation = 0;
        lrotation = 0;

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation > thirddistance) {
                leftpwr = 0;
            }
            if (rrotation > - thirddistance) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation += (right.getCurrentPosition() - truer);
            lrotation += (left.getCurrentPosition() - truel);
            truel = left.getCurrentPosition();
            truer = right.getCurrentPosition();

            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);

            waitOneFullHardwareCycle();
        }

        leftpwr = fourthpower;
        rightpwr = fourthpower;
        rrotation = 0;
        lrotation = 0;

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation > fourthdistance) {
                leftpwr = 0;
            }
            if (rrotation > - fourthdistance) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation += (right.getCurrentPosition() - truer);
            lrotation += (left.getCurrentPosition() - truel);
            truel = left.getCurrentPosition();
            truer = right.getCurrentPosition();

            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);

            waitOneFullHardwareCycle();
        }
    }
}
