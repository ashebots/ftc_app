package com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket;


import com.qualcomm.ftcrobotcontroller.opmodes.res_q.auto_basket.components.AutoBasketStartDelay;

public class AutoBasketRedDelay extends AutoBasketRed
{
    public AutoBasketRedDelay()
    {
        super.startDelaySeconds = AutoBasketStartDelay.getTimeDelay();
    }
}
