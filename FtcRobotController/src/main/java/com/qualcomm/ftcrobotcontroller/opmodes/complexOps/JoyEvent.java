package com.qualcomm.ftcrobotcontroller.opmodes.complexOps;

//Returns if a joystick is being used or not, also the motor values associated with the joystick x and y.
public class JoyEvent {
    boolean old = false;
    double fwdPower;
    double trnPower;
    double maxTurnW;
    double limit;
    public int joymode = 0;
    public JoyEvent(double FSpd,double TSpd,double mtw,double l) {
        fwdPower = FSpd;
        trnPower = TSpd;
        maxTurnW = mtw;
        limit = l;
    }

    public String parse(double x, double y) {
        boolean b = (x!=0.0 || y!=0.0);
        String a = "UNPRESSED"; //If the joystick is at 0,0
        if (b&&old) a = "HELD"; //If the joystick is being used
        if (!b&&old) a = "RELEASED"; //If the joystick just stopped being used
        if (b&&!old) a = "PRESSED"; //If the joystick is starting to be used
        old = (x!=0.0 || y!=0.0);
        return a;
    }
    public double[] calc(double x, double y) {
        //corrects for any rotational glitch in the joystick
        double[] point = {x,y};
        double[] rPoint = correctJM(point);
        x = rPoint[0];
        y = rPoint[1];

        //Calculates how fast the motors should go.
        double distance = Math.sqrt(x*x+y*y);
        if (Math.abs(distance)>1.0) {
            distance = 1.0;
        }
        double[] straight = {1.0,1.0};
        double[] turn = {1.0,-1.0};
        if (x<0) { //Determines direction of turn
            turn[0] *= -1.0;
            turn[1] *= -1.0;
        }
        if (y>0) { //Determines direction of straight line
            straight[0] *= -1.0;
            straight[1] *= -1.0;
            turn[0] *= -1.0;
            turn[1] *= -1.0;
        }
        //Does trigonometry to figure out what percentage of the joystick is straight and what is turn.
        double angle = Math.asin(y);
        angle /= Math.PI/2;
        angle = Math.abs(angle);
        double nAngle = (1-maxTurnW)*(angle+maxTurnW);

        double[] movement = {0.0,0.0};
        if (angle < limit) { //Sharp Turns
            nAngle = maxTurnW;
            if (y>0) { //Sharp turn is always forward, remove backward additions if possible
                turn[0] *= -1.0;
                turn[1] *= -1.0;
            }
        }
        //Returns a weighted average of straight and turn dependant on the joystick angle.
        movement[0] = ((straight[0]*nAngle)*fwdPower + (turn[0]*(1-nAngle))*trnPower)*distance;
        movement[1] = ((straight[1]*nAngle)*fwdPower + (turn[1]*(1-nAngle))*trnPower)*distance;
        return movement;
    }
    public double[] correctJM(double[] point) {
        if (joymode == 2) { //backwards
            double[] nPoint = {-point[0],-point[1]};
            return nPoint;
        }
        if (joymode == 1) { //C90 degrees
            double[] nPoint = {point[1],-point[0]};
            return nPoint;
        }
        if (joymode == 3) { //CC90 degrees
            double[] nPoint = {-point[1],point[0]};
            return nPoint;
        }
        return point;
    }
}