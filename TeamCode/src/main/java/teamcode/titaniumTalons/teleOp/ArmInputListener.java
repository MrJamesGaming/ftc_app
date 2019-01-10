package teamcode.titaniumTalons.teleOp;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Timer;
import java.util.TimerTask;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.SingletonOpMode;

class ArmInputListener {

    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private Timer timer;

    private static final double MANUAL_ARM_BASE_MOTOR_SPEED = 0.5;
    private static final double MANUAL_ELBOW_MOTOR_SPEED = 0.5;
    private static final double WRIST_SERVO_ADJUST_DELTA = 0.05;

    private boolean intakeGateOpened = false;
    private boolean gateOnCooldown = false;
    private boolean wristAdjustButtonDownLastUpdate = false;

    ArmInputListener() {
        gamepad1 = SingletonOpMode.instance.gamepad1;
        gamepad2 = SingletonOpMode.instance.gamepad2;
        timer = new Timer();
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    update();
                }
            }

        }.start();
    }

    private void update() {
        presetUpdate();
        baseInputUpdate();
        elbowInputUpdate();
        wristInputUpdate();
        intakeSpeedUpdate();
        intakeInputUpdate();
        gateInputUpdate();
    }

    private void presetUpdate() {
        if (gamepad1.y) {
            if (Arm.getStatus() == Arm.ArmStatus.EXTENDED) {
                Arm.retract(true);
            }
        } else if (gamepad1.a) {
            if (Arm.getStatus() == Arm.ArmStatus.RETRACTED) {
                Arm.extend(true);
            }
        }
    }

    private void baseInputUpdate() {
        if (gamepad1.dpad_up) {
            Arm.rotateArmBaseIndefinite(-MANUAL_ARM_BASE_MOTOR_SPEED);
        } else if (gamepad1.dpad_down) {
            Arm.rotateArmBaseIndefinite(MANUAL_ARM_BASE_MOTOR_SPEED);
        } else {
            Arm.lockBaseMotors();
        }
    }

    private void elbowInputUpdate() {
        if (gamepad2.dpad_left) {
            Arm.rotateElbowIndefinite(MANUAL_ELBOW_MOTOR_SPEED);
        } else if (gamepad2.dpad_right) {
            Arm.rotateElbowIndefinite(-MANUAL_ELBOW_MOTOR_SPEED);
        } else {
            Arm.lockElbow();
        }
    }

    private void wristInputUpdate() {
        if (!wristAdjustButtonDownLastUpdate) {
            if (gamepad2.right_bumper) {
                double currentWristPos = Arm.getWristServoPos();
                Arm.setWristServoPos(currentWristPos - WRIST_SERVO_ADJUST_DELTA);
                wristAdjustButtonDownLastUpdate = true;
            } else if (gamepad2.left_bumper) {
                double currentWristPos = Arm.getWristServoPos();
                Arm.setWristServoPos(currentWristPos + WRIST_SERVO_ADJUST_DELTA);
                wristAdjustButtonDownLastUpdate = true;
            }
        } else {
            if (!gamepad2.right_bumper && !gamepad2.left_bumper) {
                wristAdjustButtonDownLastUpdate = false;
            }
        }
    }

    private void intakeSpeedUpdate() {
        //intakeSpeed = asfd;
    }

    private void intakeInputUpdate() {
        if (gamepad1.right_trigger > gamepad1.left_trigger) {
            // intake
            Arm.setIntakePower(gamepad1.right_trigger * intakeSpeed);
        } else {
            // outtake
            Arm.setIntakePower(-gamepad1.left_trigger * intakeSpeed);
        }
    }

    private void gateInputUpdate() {
        if (gamepad1.x) {
            if (!gateOnCooldown) {
                if (intakeGateOpened) {
                    Arm.closeIntakeGate();
                    startGateCooldown();
                } else {
                    Arm.openIntakeGate();
                    startGateCooldown();
                }
                intakeGateOpened = !intakeGateOpened;
            }
        }
    }

    private void startGateCooldown() {
        gateOnCooldown = true;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                gateOnCooldown = false;
            }
        };
        timer.schedule(task, 500);
    }

}
