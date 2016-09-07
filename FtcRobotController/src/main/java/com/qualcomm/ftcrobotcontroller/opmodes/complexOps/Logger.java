package com.qualcomm.ftcrobotcontroller.opmodes.complexOps;

import android.util.Log;

/**
 * Created by apple on 8/25/16.
 */
public class Logger {
    public boolean DEBUG_ON = false;
    public boolean ERROR_ON = true;
    public void logDebug(String message) {
        if (DEBUG_ON) Log.i("FtcRobotController", message);
    }
    public void logError(String message) {
        if (ERROR_ON) Log.i("FtcRobotController", message);
    }
}
