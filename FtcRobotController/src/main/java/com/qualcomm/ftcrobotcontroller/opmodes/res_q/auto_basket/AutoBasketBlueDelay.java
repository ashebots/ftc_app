package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket;


import com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.components.AutoBasketStartDelay;

public class AutoBasketBlueDelay extends AutoBasketBlue
{
    public AutoBasketBlueDelay()
    {
        super.startDelaySeconds = AutoBasketStartDelay.getTimeDelay();
    }
}
