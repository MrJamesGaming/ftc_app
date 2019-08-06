package teamcode.common;

import android.text.method.TextKeyListener;

import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
@TeleOp(name = "ArmTest")
public class ArmLiftPrototype extends LinearOpMode {

    private static final double DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED = 20;
    DcMotor linearSlideLift;
    DcMotor armRotation;

    @Override
    public void runOpMode() {
        linearSlideLift = hardwareMap.get(DcMotor.class, "linearSlideLift");
        armRotation = hardwareMap.get(DcMotor.class, "armRotation");
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("status", "enter");
            telemetry.update();
            if (gamepad1.right_trigger > 0.3) {
                moveSlideUp();
            } else if (gamepad1.left_trigger > 0.3) {
                moveSlideDown();
            }
            telemetry.addData("status", "exit");
            telemetry.update();
            if (gamepad1.dpad_down) {
                telemetry.addData("Statue:", "Down");
                telemetry.update();
                rotateArmDown();
            } else if (gamepad1.dpad_up) {
                telemetry.addData("Statue:", "Up");
                telemetry.update();
                rotateArmUp();
            }
            if(gamepad1.b){
                rotateArmDegrees(15);
            }
            if(gamepad1.y){
                rotateArmDegrees(45);
            }
        }
    }

    private void rotateArmDegrees(int degrees) {
        armRotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int)(degrees * DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED);
        armRotation.setTargetPosition(ticks);
        armRotation.setPower(0.1);
        while(!armRotation.isBusy()){
            //incorrect, just needed something to get this to compile until tomorrow
        }
    }



    private void rotateArmUp() {
        while (gamepad1.dpad_up) {
            armRotation.setPower(.1);
        }
        armRotation.setPower(0);
    }

    private void rotateArmDown() {
        while (gamepad1.dpad_down) {
            armRotation.setPower(-.1);
        }
        armRotation.setPower(0);
    }

    private void moveSlideDown() {
        while (gamepad1.left_trigger > 0.3) {
            linearSlideLift.setPower(-1);
        }
        linearSlideLift.setPower(0);
    }

    private void moveSlideUp() {
        while (gamepad1.right_trigger > 0.3) {
            linearSlideLift.setPower(1);
        }
        linearSlideLift.setPower(0);
    }
}
//linear slide lift is controlled by triggers
//arm rotation is DPad
//B Button moves 15 degrees and Y Button moves 45 degrees