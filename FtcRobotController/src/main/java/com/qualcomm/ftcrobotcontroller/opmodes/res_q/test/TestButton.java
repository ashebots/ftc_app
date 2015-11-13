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
        double firstpower = 0.5;
        double secondpower = 0.5;
        double thirdpower = 0.5;
        double fourthpower = 0.5;
        double fifthpower = 0.5;

        int firstdistance = 5500;
        int seconddistanceleft = 9000;
        int seconddistanceright = 4500;
        int thirddistance = 770;
        int fourthdistance = 800;
        double fifthdistance = (((18 * Math.sqrt(2)) - 18) / 2) * 1000;

        double ttpower = secondpower * seconddistanceright / seconddistanceleft;

        double leftpwr = firstpower;
        double rightpwr = firstpower;
        int rrotation = 0;
        int lrotation = 0;
        int truel = left.getCurrentPosition();
        int truer = right.getCurrentPosition();

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation > firstdistance) {
                leftpwr = 0;
            }
            if (rrotation > firstdistance) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation = (right.getCurrentPosition() - truer);
            lrotation = (left.getCurrentPosition() - truel);

            telemetry.addData("STEP", 1);
            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);
            telemetry.addData("4: Start L", truel);
            telemetry.addData("5: Start R", truer);

            waitOneFullHardwareCycle();
        }

        leftpwr = secondpower;
        rightpwr = ttpower;
        rrotation = 0;
        lrotation = 0;
        truel = left.getCurrentPosition();
        truer = right.getCurrentPosition();

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation > seconddistanceleft) {
                leftpwr = 0;
            }
            if (rrotation > seconddistanceright) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation = (right.getCurrentPosition() - truer);
            lrotation = (left.getCurrentPosition() - truel);

            telemetry.addData("STEP", 2);
            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);
            telemetry.addData("4: Start L", truel);
            telemetry.addData("5: Start R", truer);

            waitOneFullHardwareCycle();
        }

        leftpwr = thirdpower;
        rightpwr = - thirdpower;
        rrotation = 0;
        lrotation = 0;
        truel = left.getCurrentPosition();
        truer = right.getCurrentPosition();

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation > thirddistance) {
                leftpwr = 0;
            }
            if (rrotation < - thirddistance) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation = (right.getCurrentPosition() - truer);
            lrotation = (left.getCurrentPosition() - truel);

            telemetry.addData("STEP", 3);
            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);
            telemetry.addData("4: Start L", truel);
            telemetry.addData("5: Start R", truer);

            waitOneFullHardwareCycle();
        }

        leftpwr = fourthpower;
        rightpwr = fourthpower;
        rrotation = 0;
        lrotation = 0;
        truel = left.getCurrentPosition();
        truer = right.getCurrentPosition();

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation > fourthdistance) {
                leftpwr = 0;
            }
            if (rrotation > - fourthdistance) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation = (right.getCurrentPosition() - truer);
            lrotation = (left.getCurrentPosition() - truel);

            telemetry.addData("STEP", 4);
            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);
            telemetry.addData("4: Start L", truel);
            telemetry.addData("5: Start R", truer);

            waitOneFullHardwareCycle();
        }

        leftpwr = - fifthpower;
        rightpwr = - fifthpower;
        rrotation = 0;
        lrotation = 0;
        truel = left.getCurrentPosition();
        truer = right.getCurrentPosition();

        while (rightpwr > 0 || leftpwr > 0) {
            if (lrotation < - fifthdistance) {
                leftpwr = 0;
            }
            if (rrotation < - fifthdistance) {
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation = (right.getCurrentPosition() - truer);
            lrotation = (left.getCurrentPosition() - truel);

            telemetry.addData("STEP", 3);
            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);
            telemetry.addData("4: Start L", truel);
            telemetry.addData("5: Start R", truer);

            waitOneFullHardwareCycle();
        }
        leftpwr = sixthpower;
        rightpwr = - sixthpower;
        rrotation = 0;
        lrotation = 0;
        truel = left.getCurrentPosition();
        truer = right.getCurrentPosition();

        while (rightpwr > 0 || leftpwr > 0) {
            if () {
                leftpwr = 0;
                rightpwr = 0;
            }
            left.setPower(leftpwr);
            right.setPower(rightpwr);

            rrotation = (right.getCurrentPosition() - truer);
            lrotation = (left.getCurrentPosition() - truel);

            telemetry.addData("STEP", 3);
            telemetry.addData("1: Time Elapsed", getRuntime());
            telemetry.addData("2: Left Motor", lrotation);
            telemetry.addData("3: Right Motor", rrotation);
            telemetry.addData("4: Start L", truel);
            telemetry.addData("5: Start R", truer);

            waitOneFullHardwareCycle();
        }
    }
}