package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.components;


public class AutoBasketStartDelay
{
    private static double delaySeconds = 10;

    //Time in seconds that basket autonomous should be delayed.
    //Only used in 2 program variations. The other 2 variations don't have any delay
    public static double getTimeDelay()
    {
        //Make sure that the variable was properly initialize
        if (Double.isNaN(delaySeconds))
        {
            return 0;
        }
        else
        {
            return delaySeconds;
        }
    }
}
