package com.qualcomm.ftcrobotcontroller.opmodes.past.res_q.art;

/**
 * Created by Art Schell on 12/8/2015.
 */



public abstract class StrategyBlocks extends Driving {
    protected String[] strategy = new String[8];

    public void moveToNextSquare(String begin, String end) throws InterruptedException {
        if ((begin == "3" && end == "2") || (begin == "2" && end == "1") || (begin == "a" && end == "d") || (begin == "b" && end == "e") || (begin == "e" && end == "z") || (begin == "c" && end == "h")) {
            moveForwardCorrectionBackground(18, 0.75, 0.25, 10, 3);
        }
        else if ((begin == "2" && end == "3") || (begin == "1" && end == "2") || (begin == "d" && end == "a") || (begin == "e" && end == "b") || (begin == "z" && end == "e") || (begin == "h" && end == "c")) {
            moveForwardCorrectionBackground(18, 0.75, 0.25, 10, 3);
        }
        else if ((begin == "2" && end == "a") || (begin == "1" && end == "d") || (begin == "d" && end == "b") || (begin == "e" && end == "c") || (begin == "z" && end == "h")) {
            moveForwardCorrectionBackground(18, 0.75, 0.25, 10, 3);
        }
        else if ((begin == "a" && end == "2") || (begin == "d" && end == "1") || (begin == "b" && end == "d") || (begin == "c" && end == "e") || (begin == "h" && end == "z")) {
            moveForwardCorrectionBackground(18, 0.75, 0.25, 10, 3);
        }
        else if ((begin == "2" && end == "d") || (begin == "d" && end == "e") || (begin == "e" && end == "h") || (begin == "a" && end == "b") || (begin == "b" && end == "c")) {
            moveForwardCorrectionBackground(18*1.414, 0.75, 0.25, 10, 3);
        }
        else if ((begin == "d" && end == "2") || (begin == "e" && end == "d") || (begin == "h" && end == "e") || (begin == "b" && end == "a") || (begin == "c" && end == "b")) {
            moveForwardCorrectionBackground(18*1.414, 0.75, 0.25, 10, 3);
        }
        else if ((begin == "a" && end == "1") || (begin == "c" && end == "z")) {
            moveForwardCorrectionBackground(18*1.414, 0.75, 0.25, 10, 3);
        }
        else if ((begin == "1" && end == "a") || (begin == "z" && end == "c")) {
            moveForwardCorrectionBackground(18*1.414, 0.75, 0.25, 10, 3);
        }
        else {
            moveForwardCorrectionBackground(18*1.414, 0.75, 0.25, 10, 3);
        }
    }

    public void initMove(String begin, String end) throws InterruptedException {
        if ((begin == "3" && end == "2") || (begin == "2" && end == "1") || (begin == "a" && end == "d") || (begin == "b" && end == "e") || (begin == "e" && end == "z") || (begin == "c" && end == "h")) {
            moveForwardCorrectionBackInit(90);
        }
        else if ((begin == "2" && end == "3") || (begin == "1" && end == "2") || (begin == "d" && end == "a") || (begin == "e" && end == "b") || (begin == "z" && end == "e") || (begin == "h" && end == "c")) {
            moveForwardCorrectionBackInit(-90);
        }
        else if ((begin == "2" && end == "a") || (begin == "1" && end == "d") || (begin == "d" && end == "b") || (begin == "e" && end == "c") || (begin == "z" && end == "h")) {
            moveForwardCorrectionBackInit(0);
        }
        else if ((begin == "a" && end == "2") || (begin == "d" && end == "1") || (begin == "b" && end == "d") || (begin == "c" && end == "e") || (begin == "h" && end == "z")) {
            moveForwardCorrectionBackInit(180);
        }
        else if ((begin == "2" && end == "d") || (begin == "d" && end == "e") || (begin == "e" && end == "h") || (begin == "a" && end == "b") || (begin == "b" && end == "c")) {
            moveForwardCorrectionBackInit(45);
        }
        else if ((begin == "d" && end == "2") || (begin == "e" && end == "d") || (begin == "h" && end == "e") || (begin == "b" && end == "a") || (begin == "c" && end == "b")) {
            moveForwardCorrectionBackInit(-135);
        }
        else if ((begin == "a" && end == "1") || (begin == "c" && end == "z")) {
            moveForwardCorrectionBackInit(-45);
        }
        else if ((begin == "1" && end == "a") || (begin == "z" && end == "c")) {
            moveForwardCorrectionBackInit(-45);
        }
        else {
            moveForwardCorrectionBackInit(135);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //init

        initMotors();
        initBNO055();

        waitForStart();
        long startTime = systemTime;

        int stepProg = 1;
        boolean initStep = true;
        boolean stepDone = false;

        while(true) {
            if (initStep) {
                switch(stepProg) {
                    case 1:
                        moveForwardCorrectionBackInit(0);
                        break;
                    case 2:
                        initMove(strategy[0], strategy[1]);
                        break;
                    case 3:
                        initMove(strategy[1],strategy[2]);
                        break;
                    case 4:
                        initMove(strategy[2],strategy[3]);
                        break;
                    case 5:
                        initMove(strategy[3],strategy[4]);
                        break;
                    case 6:
                        initMove(strategy[4],strategy[5]);
                        break;
                    case 7:
                        initMove(strategy[5],strategy[6]);
                        break;
                    case 8:
                        initMove(strategy[6],strategy[7]);
                        break;
                }
                initStep = false;
            }

            if (!forwardFinish) {
                switch (stepProg) {
                    case 1:
                        moveForwardCorrectionBackground(3,1,0.25,10,3);
                        break;
                    case 2:
                        moveToNextSquare(strategy[0], strategy[1]);
                        break;
                    case 3:
                        moveToNextSquare(strategy[1], strategy[2]);
                        break;
                    case 4:
                        moveToNextSquare(strategy[2], strategy[3]);
                        break;
                    case 5:
                        moveToNextSquare(strategy[3], strategy[4]);
                        break;
                    case 6:
                        moveToNextSquare(strategy[4], strategy[5]);
                        break;
                    case 7:
                        moveToNextSquare(strategy[5], strategy[6]);
                        break;
                    case 8:
                        moveToNextSquare(strategy[6],strategy[7]);
                        break;
                }
            }
            if (((systemTime - startTime) % 3750) < 20 || 3730 < ((systemTime - startTime) % 3750)) {
               initStep = true;
                stepProg++;
            }

            //arm code if neccesary

            waitOneFullHardwareCycle();
        }

    }

}
