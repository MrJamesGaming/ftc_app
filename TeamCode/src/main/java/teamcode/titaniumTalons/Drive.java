package teamcode.titaniumTalons;

import com.qualcomm.robotcore.hardware.DcMotor;

import teamcode.Vector2;

// USEFUL INFORMATION FOR MECANUM WHEEL MATH:
// https://pmtischler-ftc-app.readthedocs.io/en/latest/tutorials/mecanum.html

/**
 * Contains methods pertaining to the drive system of the robot.
 */
public final class Drive {

    /**
     * The number of ticks that each drive motor will have to turn to make the robot drive
     * vertically one inch.
     */
    private static final double DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_VERTICAL = 90.2;
    /**
     * The number of ticks that each drive motor will have to turn to make the robot drive
     * laterally one inch.
     */
    private static final double DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_LATERAL = 104.0;
    /**
     * The number of ticks that each drive motor will have to turn to make the robot turn one
     * degree.
     */
    private static final double DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED = 22.7111913357;

    /**
     * Causes the robot to drive vertically a definite, specified number of inches. The thread from
     * which this method is called is forced to wait until the robot finishes driving.
     *
     * @param inches The number of inches that the robot should travel. If positive, the robot
     *               will drive forward. If negative, the robot will drive backward.
     * @param power  the power that the drive motors should be set to
     */
    public static void driveVerticallyDefinite(double inches, double power) {
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_VERTICAL);

        HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        HardwareManager.frontRightDrive.setTargetPosition(ticks);
        HardwareManager.backLeftDrive.setTargetPosition(ticks);
        HardwareManager.backRightDrive.setTargetPosition(ticks);

        HardwareManager.frontLeftDrive.setPower(power);
        HardwareManager.frontRightDrive.setPower(power);
        HardwareManager.backLeftDrive.setPower(power);
        HardwareManager.backRightDrive.setPower(power);

        while (SingletonOpMode.active() && driveMotorsBusy()) ;
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Causes the robot to drive laterally a definite, specified number of inches. The thread from
     * which this method is called is forced to wait until the robot finishes driving.
     *
     * @param inches The number of inches that the robot should travel. If positive, the robot
     *               will drive to the right. If negative, the robot will drive to the left.
     * @param power  the power that the drive motors should be set to
     */
    public static void driveLateralDefinite(double inches, double power) {
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_LATERAL);

        HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        HardwareManager.frontRightDrive.setTargetPosition(-ticks);
        HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        HardwareManager.backRightDrive.setTargetPosition(ticks);

        HardwareManager.frontLeftDrive.setPower(power);
        HardwareManager.frontRightDrive.setPower(power);
        HardwareManager.backLeftDrive.setPower(power);
        HardwareManager.backRightDrive.setPower(power);

        while (SingletonOpMode.active() && driveMotorsBusy()) ;
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Causes the robot to drive an indefinite distance in the direction of the specified vector
     * until the motors are explicitly told to do otherwise. The power of the motors are set to the
     * magnitude of the specified vector. The thread from which this method is called is NOT forced
     * to wait until the robot finishes driving.
     *
     * @param v         The vector that the robot should use to drive. It's magnitude is the
     *                  power that the drive motors should be set to.
     * @param turnSpeed The speed at which the robot should change the angle it is driving
     *                  at. A non-zero value will make the robot drive in an arc. A positive value
     *                  will make the robot turn clockwise, whereas a negative value will make the
     *                  robot turn counterclockwise.
     */
    public static void driveIndefinite(Vector2 v, double turnSpeed) {
        setDriveRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double direction = Math.atan2(-v.getX(), v.getY());
        double power = v.magnitude();

        double frontLeftPow = power * Math.sin(-direction + Math.PI / 4) + turnSpeed;
        double frontRightPow = power * Math.cos(-direction + Math.PI / 4) - turnSpeed;
        double backLeftPow = power * Math.cos(-direction + Math.PI / 4) + turnSpeed;
        double backRightPow = power * Math.sin(-direction + Math.PI / 4) - turnSpeed;

        HardwareManager.frontLeftDrive.setPower(frontLeftPow);
        HardwareManager.frontRightDrive.setPower(frontRightPow);
        HardwareManager.backLeftDrive.setPower(backLeftPow);
        HardwareManager.backRightDrive.setPower(backRightPow);
    }

    /**
     * Causes the robot to turn a definite number of degrees at the specified power. The thread
     * from which this method is called is forced to wait until the robot finishes turning.
     *
     * @param degrees The degrees that the robot should turn. The robot will turn clockwise if this
     *                value is positive, and will turn counterclockwise if this value is negative.
     * @param power   the power that the drive motors should be set to
     */
    public static void turnDefinite(double degrees, double power) {
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (degrees * DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED);

        HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        HardwareManager.frontRightDrive.setTargetPosition(-ticks);
        HardwareManager.backLeftDrive.setTargetPosition(ticks);
        HardwareManager.backRightDrive.setTargetPosition(-ticks);

        HardwareManager.frontLeftDrive.setPower(power);
        HardwareManager.frontRightDrive.setPower(power);
        HardwareManager.backLeftDrive.setPower(power);
        HardwareManager.backRightDrive.setPower(power);

        while (SingletonOpMode.active() && driveMotorsBusy()) ;
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Causes the robot to turn an indefinite angle until the motors are explicitly told
     * to do otherwise. The thread from which this method is called is NOT forced
     * to wait until the robot finishes driving.
     *
     * @param power The power that the drive motors should be set to. If positive, the robot will
     *              turn clockwise. If negative, the robot will turn counterclockwise.
     */
    public static void turnIndefinite(double power) {
        setDriveRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    /**
     * Sets the {@code RunMode} of each drive motor to the specified mode.
     */
    private static void setDriveRunMode(DcMotor.RunMode runMode) {
        HardwareManager.frontLeftDrive.setMode(runMode);
        HardwareManager.frontRightDrive.setMode(runMode);
        HardwareManager.backLeftDrive.setMode(runMode);
        HardwareManager.backRightDrive.setMode(runMode);
    }

    /**
     * Returns whether the drive motors are running.
     */
    private static boolean driveMotorsBusy() {
        return HardwareManager.frontLeftDrive.isBusy()
                || HardwareManager.frontRightDrive.isBusy()
                || HardwareManager.backLeftDrive.isBusy()
                || HardwareManager.backRightDrive.isBusy();
    }

}
