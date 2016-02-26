/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.res_q.teleop.TeleOp;
import com.qualcomm.ftcrobotcontroller.opmodes.res_q.test.*;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * Register Op Modes
 */
public class FtcOpModeRegister implements OpModeRegister {

  /**
   * The Op Mode Manager will call this method when it wants a list of all
   * available op modes. Add your op mode to the list to enable it.
   *
   * @param manager op mode manager
   */
  public void register(OpModeManager manager) {

    /*
     * register your op modes here.
     * The first parameter is the name of the op mode
     * The second parameter is the op mode class property
     *
     * If two or more op modes are registered with the same name, the app will display an error.
     */

    //manager.register("Atlas: TeleOp", AtlasTeleop.class);
    //manager.register("Beacon: TeleOp", BeaconTeleop.class);

    //manager.register("Cascade Effect TeleOp", CascadeEffectTeleop.class);
    //manager.register("Telemetry Test", TelemetryTest.class);
    manager.register("TeleOp", TeleOp.class);

    manager.register("Blue Autonomous Mountain", ClimbersToMountainBlueM.class);
    manager.register("Red Autonomous Mountain", ClimbersToMountainRedM.class);
    manager.register("Blue Autonomous Mountain 2ndPos", ClimbersToMountainBlueOffsetM.class);
    manager.register("Red Autonomous Mountain 2ndPos", ClimbersToMountainRedOffsetM.class);

    manager.register("Mountain Simple", MountainSimple.class);

    manager.register("Mountain MidZone", MountainCharge.class);
    //manager.register("Blue Autonomous Climbers", ClimbersToMountainBlue.class);
    //manager.register("Red Autonomous Climbers", ClimbersToMountainRed.class);
    //manager.register("Blue Autonomous Climbers 2ndPos", ClimbersToMountainBlueOffset.class);
    //manager.register("Red Autonomous Climbers 2ndPos", ClimbersToMountainRedOffset.class);

    //manager.register("Background Test", StrategyBlocks.class);

    //manager.register("Accelerometer Test", IMUtest.class);

    manager.register("IMU Heading Test", IMUtest.class);

    //manager.register("Test Drive", TestDrive.class);

    //manager.register("Background Movement", TestAutoMountainBCCopy.class);

    //manager.register("Strategy Test", StrategyTest.class);

    //manager.register("Sonic", SonicRobotEyes.class);

    /*
     * The following op modes are example op modes provided by QualComm.
     * Uncomment the lines to make the op modes available to the driver station.
     */
    //manager.register("LinearK9TeleOp", LinearK9TeleOp.class);
    //manager.register("LinearIrExample", LinearIrExample.class);
    //manager.register("IrSeekerOp", IrSeekerOp.class);
    //manager.register("CompassCalibration", CompassCalibration.class);
    //manager.register("NxtTeleOp", NxtTeleOp.class);

  }
}
