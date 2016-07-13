package com.qualcomm.ftcrobotcontroller.opmodes.past.auto_basket;


import com.qualcomm.ftcrobotcontroller.opmodes.past.auto_basket.components.AutoBasketStartDelay;

public class AutoBasketBlueDelay extends AutoBasketBlue
{
    public AutoBasketBlueDelay()
    {
        super.startDelaySeconds = AutoBasketStartDelay.getTimeDelay();
    }
}
