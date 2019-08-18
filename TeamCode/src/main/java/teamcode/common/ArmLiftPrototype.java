package teamcode.common;

import android.text.method.TextKeyListener;

import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
@TeleOp(name = "ArmTest")
public class ArmLiftPrototype extends LinearOpMode {

    private static final double DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED = 3.506493506;
    DcMotor linearSlideLift;
    DcMotor armRotation;

    @Override
    public void runOpMode() throws InterruptedException {
        linearSlideLift = hardwareMap.get(DcMotor.class, "linearSlideLift");
        armRotation = hardwareMap.get(DcMotor.class, "armRotation");
        armRotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();

        while (opModeIsActive()) {
            toPosA();
            if (gamepad1.right_trigger > 0.3) {
                moveSlideUp();
            } else if (gamepad1.left_trigger > 0.3) {
                moveSlideDown();
            }
            if (gamepad1.dpad_down) {
                    rotateArmDegrees(-5);
                    Thread.sleep(100);
                    //do NOT use sleep
                    if(gamepad1.dpad_down){
                        rotateArmDown();
                    }
            } else if (gamepad1.dpad_up) {
                    rotateArmDegrees(5);
                    Thread.sleep(100);
                    if(gamepad1.dpad_up){
                        rotateArmUp();
                    }

            }else if(gamepad1.b){
                rotateArmDegrees(15);
            }else if(gamepad1.y){
                rotateArmDegrees(45);
            } else if(gamepad1.a){
                rotateArmDegrees(-45);
            }else if(gamepad1.x){
                rotateArmDegrees(-15);
            }
        }
    }

    private void rotateArmDPad(int degrees) {
        armRotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int)(degrees * DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED);
        armRotation.setTargetPosition(ticks);
        armRotation.setPower(1);
    }

    private void rotateArmDegrees(int degrees) {
        armRotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int)(degrees * DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED);
        armRotation.setTargetPosition(ticks);
        armRotation.setPower(1);

    }
    private void toPosA(){

        telemetry.addData("Current Pos", String.valueOf(armRotation.getCurrentPosition()));
        telemetry.update();
        telemetry.addData("Current Slide Pos", String.valueOf(linearSlideLift.getCurrentPosition()));
        telemetry.update();

    }



    private void rotateArmUp() {
        armRotation.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        while (gamepad1.dpad_up) {
            armRotation.setPower(1);
        }
        armRotation.setPower(0);
    }

    private void rotateArmDown() {
        armRotation.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        while (gamepad1.dpad_down) {
            armRotation.setPower(-1);
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
//B Button moves 15 degrees and Y Button moves 45 degrees, opposite on the