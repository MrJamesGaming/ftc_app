package teamcode.titaniumTalons;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Contains methods pertaining to the arm system of the robot.
 */
public final class Arm {

    private static final double BASE_MOTOR_TICKS_PER_DEGREE = 5.73333333333;
    private static final double ELBOW_MOTOR_TICKS_PER_DEGREE = 0.8;

    private static ArmStatus status;

    public enum ArmStatus {
        EXTENDED, RETRACTED
    }

    public static ArmStatus getStatus() {
        return status;
    }

    public static void extend() {
        if (status == ArmStatus.EXTENDED) {
            throw new IllegalStateException("Arm is already extended!");
        }
        setWristServoPos(0.65);
        rotateArmBaseDefinite(100.0, 1.0);
        status = ArmStatus.EXTENDED;
    }

    public static void retract() {
        if (status == ArmStatus.RETRACTED) {
            throw new IllegalStateException("Arm is already retracted!");
        }
        setWristServoPos(0.3);
        rotateArmBaseDefinite(-100.0, 1.0);
        status = ArmStatus.RETRACTED;
    }

    /**
     * @param degrees make positive to extend, negative to retract
     * @param power
     */
    public static void rotateArmBaseDefinite(double degrees, double power) {
        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int pos = (int) (degrees * BASE_MOTOR_TICKS_PER_DEGREE);
        HardwareManager.leftArmBaseMotor.setTargetPosition(pos);
        HardwareManager.rightArmBaseMotor.setTargetPosition(pos);
        HardwareManager.leftArmBaseMotor.setPower(power);
        HardwareManager.rightArmBaseMotor.setPower(power);
        while (SingletonOpMode.active() &&
                (HardwareManager.leftArmBaseMotor.isBusy() ||
                        HardwareManager.rightArmBaseMotor.isBusy())) ;
    }

    /**
     * @param power make positive to extend, negative to retract
     */
    public static void rotateArmBaseIndefinite(double power) {
        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        HardwareManager.leftArmBaseMotor.setPower(power);
        HardwareManager.rightArmBaseMotor.setPower(power);
    }

    /**
     * @param degrees make positive to extend, negative to retract
     * @param power
     */
    public static void rotateElbowDefinite(double degrees, double power) {
        HardwareManager.armElbowMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.armElbowMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int pos = (int) (degrees * ELBOW_MOTOR_TICKS_PER_DEGREE);
        HardwareManager.armElbowMotor.setTargetPosition(pos);
        HardwareManager.armElbowMotor.setPower(power);
        while (SingletonOpMode.active() &&
                HardwareManager.armElbowMotor.isBusy()) ;
    }

    /**
     * @param pos [0.0, 1.0] make higher to extend
     */
    public static void setWristServoPos(double pos) {
        HardwareManager.leftArmWristServo.setPosition(pos);
        HardwareManager.rightArmWristServo.setPosition(pos);
    }

    /**
     * Opens the intake gate, allowing balls to exit the intake chamber.
     */
    public static void openIntakeGate() {
        HardwareManager.intakeGateServo.setPosition(0.5);
    }

    /**
     * Closes the intake gate, trapping balls inside the intake chamber.
     */
    public static void closeIntakeGate() {
        HardwareManager.intakeGateServo.setPosition(0.0);
    }

    /**
     * @param power make positive to intake, negative to exhale
     */
    public static void setIntakePower(double power) {
        HardwareManager.intakeMotor.setPower(power);
    }

    /**
     * Resets the arm status to {@link ArmStatus#EXTENDED}.
     */
    public static void reset() {
        status = ArmStatus.EXTENDED;
    }

}