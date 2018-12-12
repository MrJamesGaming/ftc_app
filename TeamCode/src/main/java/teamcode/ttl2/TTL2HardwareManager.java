package teamcode.ttl2;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

class TTL2HardwareManager {

    private static final String FRONT_LEFT_DRIVE_NAME = "FrontLeftDrive";
    private static final String FRONT_RIGHT_DRIVE_NAME = "FrontRightDrive";
    private static final String BACK_LEFT_DRIVE_NAME = "BackLeftDrive";
    private static final String BACK_RIGHT_DRIVE_NAME = "BackRightDrive";
    private static final String LEFT_ARM_BASE_MOTOR_NAME = "LiftMotorL";
    private static final String RIGHT_ARM_BASE_MOTOR_NAME = "LiftMotorR";
    private static final String LEFT_ARM_ELBOW_SERVO_NAME = "LeftArmElbowServo";
    private static final String RIGHT_ARM_ELBOW_SERVO_NAME = "RightArmElbowServo";
    private static final String ARM_WRIST_SERVO_NAME = "ArmWristServo";
    private static final String INTAKE_SERVO_NAME = "IntakeServo";
    private static final String MARKER_CLAW_SERVO_NAME = "MarkerClawServo";

    // drive hardware
    public static DcMotor frontLeftDrive;
    public static DcMotor frontRightDrive;
    public static DcMotor backLeftDrive;
    public static DcMotor backRightDrive;

    // arm hardware
    public static DcMotor leftArmBaseMotor;
    public static DcMotor rightArmBaseMotor;
    public static Servo leftArmElbowServo;
    public static Servo rightArmElbowServo;
    public static Servo armWristServo;
    public static Servo intakeServo;

    // marker
    public static Servo markerClawServo;

    public static void initialize(HardwareMap hardwareMap) {
        frontLeftDrive = hardwareMap.get(DcMotor.class, FRONT_LEFT_DRIVE_NAME);
        frontRightDrive = hardwareMap.get(DcMotor.class, FRONT_RIGHT_DRIVE_NAME);
        backLeftDrive = hardwareMap.get(DcMotor.class, BACK_LEFT_DRIVE_NAME);
        backRightDrive = hardwareMap.get(DcMotor.class, BACK_RIGHT_DRIVE_NAME);

        frontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        leftArmBaseMotor = hardwareMap.get(DcMotor.class, LEFT_ARM_BASE_MOTOR_NAME);
        rightArmBaseMotor = hardwareMap.get(DcMotor.class, RIGHT_ARM_BASE_MOTOR_NAME);
        leftArmElbowServo = hardwareMap.get(Servo.class, LEFT_ARM_ELBOW_SERVO_NAME);
        rightArmElbowServo = hardwareMap.get(Servo.class, RIGHT_ARM_ELBOW_SERVO_NAME);
        armWristServo = hardwareMap.get(Servo.class, ARM_WRIST_SERVO_NAME);
        intakeServo = hardwareMap.get(Servo.class, INTAKE_SERVO_NAME);

        rightArmElbowServo.setDirection(Servo.Direction.REVERSE);

        markerClawServo = hardwareMap.get(Servo.class, MARKER_CLAW_SERVO_NAME);
    }

}
