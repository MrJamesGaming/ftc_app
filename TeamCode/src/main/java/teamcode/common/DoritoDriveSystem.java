package teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DoritoDriveSystem implements IDriveSystem{
    private final DcMotor leftMotor, rightMotor;
    public DoritoDriveSystem(DcMotor leftMotor, DcMotor rightMotor){
        this.rightMotor = rightMotor;
        this.leftMotor = leftMotor;
    }

    @Override
    public void move(Vector velocity, double distance) {
        int leftPos = 0;
        int rightPos = 0;
        rightMotor.setTargetPosition(rightPos);
        leftMotor.setTargetPosition(leftPos);
        rightMotor.setPower(velocity.magnitude());
        leftMotor.setPower(velocity.magnitude());

    }

    @Override
    public void moveContinuously(Vector velocity) {
        rightMotor.setPower(velocity.magnitude());
        leftMotor.setPower(velocity.magnitude());
    }

    @Override
    public void turn(double degrees, double speed) {
        if(degrees > 0){
            leftMotor.setPower(speed);

        }else{
            rightMotor.setPower(speed);
        }
    }

    @Override
    public void turnContinuously(double speed) {
        if(speed > 0){
            leftMotor.setPower(speed);
        }else{
            rightMotor.setPower(speed);
        }
    }
}
