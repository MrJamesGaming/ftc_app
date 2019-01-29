package teamcode.titaniumTalons.teleOp;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.TimerTask;

import teamcode.titaniumTalons.HardwareManager;
import teamcode.titaniumTalons.RobotTimer;

public class TeleOpLights {

    public void enable() {
        HardwareManager.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_PARTY_PALETTE);

        TimerTask shift0 = new TimerTask() {
            @Override
            public void run() {
                HardwareManager.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE_VIOLET);
            }
        };

        TimerTask shift1 = new TimerTask() {
            @Override
            public void run() {
                HardwareManager.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            }
        };

        TimerTask shift2 = new TimerTask() {
            @Override
            public void run() {
                HardwareManager.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
            }
        };

        TimerTask shift3 = new TimerTask() {
            @Override
            public void run() {
                HardwareManager.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.STROBE_RED);
            }
        };

        TimerTask shift4 = new TimerTask() {
            @Override
            public void run() {
                HardwareManager.ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.STROBE_WHITE);
            }
        };

        RobotTimer.schedule(shift0, 30.0);
        RobotTimer.schedule(shift1, 60.0);
        RobotTimer.schedule(shift2, 90.0);
        RobotTimer.schedule(shift3, 105.0);
        RobotTimer.schedule(shift4, 115.0);
    }

}
