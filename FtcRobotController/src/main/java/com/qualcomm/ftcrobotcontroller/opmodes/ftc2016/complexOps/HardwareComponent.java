package com.qualcomm.ftcrobotcontroller.opmodes.ftc2016.complexOps;

//This class is used to provide a base for all hardware, sensor, and computer functions - for example motors or timers.
public abstract class HardwareComponent {
    //Calibrate stores the current encoder position to use in relative encoder tracking.
    public abstract void calibrate();
    //GetValues calculates new positions of all sensor parts and adjusts relative encoder values.
    public abstract void getValues();
    //The Stop method stops any movement that may be going on - as in a motor or servo.
    public abstract void stop();
}
