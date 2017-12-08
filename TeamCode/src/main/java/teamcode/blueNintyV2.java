/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static java.lang.Math.sqrt;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="blueNintyV2", group="Linear Opmode")  // @Autonomous(...) is the other common choice

public class blueNintyV2 extends LinearOpMode {
    private VuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable trackable;
    private KiwiRobot robot;
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Servo jewelServo;
    private Servo armServo;
    private Servo leftClampServo;
    private Servo rightClampServo;
    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

    int cooldown = 1000;
    private boolean isClamped = true;
    private double row1Position = 0.2;
    private double row2Position = 0.4;
    private double row3Position = 0.6;
    private BNO055IMU gyro;
    final private double OPENCLAMPPOSITION = 0;
    final private double CLOSECLAMPPOSITION = .5;
    final private double LIFTEDARMPOSITION = .55;
    final private double DOWNARMPOSITION = .8;

    final private double JEWELCLOSECLAMP = .05; // close clamp
    final private double JEWELCHOPTIME = JEWELCLOSECLAMP + 1; // jewel servo down
    final private double JEWELARMRAISE = JEWELCHOPTIME + .5; // arm servo up
    final private double SPINTOWIN = JEWELARMRAISE + .1; // turn to knock off jewel
    final private double JEWELSHEATHARM = SPINTOWIN + 2; // raise arm to sheath
    final private double JEWELSTOREARM = JEWELSHEATHARM + .5; // store arm
    final private double JEWELSPINBACK = JEWELSTOREARM + .1; // turn b
    // ack to compensate for knock off jewel turn

    final private double LIFTARM = JEWELSPINBACK + 2; //lift arm
    final private double TURNTOLINEUPWITHCOLUMNS = LIFTARM + 1.3; // turn 180
    final private double PHASETHREE = TURNTOLINEUPWITHCOLUMNS + 1.8; // drive forward
    final private double PHASETHREEHALF = PHASETHREE + .1; // turn off motors
    final private double PHASEFOUR = PHASETHREEHALF + .5; // lowerarm
    final private double PHASEFIVE = PHASEFOUR + 1; // turn to face columns
    final private double PHASESIX = PHASEFIVE + 2.2; // drive stright
    final private double PHASESIXHALF = PHASESIX + .1; // turn off motors
    final private double PHASESEVEN = PHASESIXHALF + .5; // open clamp
    final private double PHASEEIGHT = PHASESEVEN + .4; // turn
    final private double PHASENINE = PHASEEIGHT + .4; // back up
    final private double PHASENINEHALF = PHASENINE + .1; // turn off motors

    final private double TURNTOWARDSGLYPHPIT = PHASENINEHALF + .6; // turn towards glyph pit
    final private double DRIVETOGLYPHPIT = TURNTOWARDSGLYPHPIT + 1; // drive to the glyph pit
    final private double GRABABLOCK = DRIVETOGLYPHPIT + 1.5; // grab a block in the pit
    final private double BACKTOBASE = GRABABLOCK + .75; // grab a block in the pit
    final private double TURNTOFACECOLUMNS = BACKTOBASE + .6; // turn to face columns
    //turns off all motors at end


    @Override
    public void runOpMode() {
        robot = new KiwiRobot(hardwareMap);
        this.initVuforia();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = false;
        parameters.loggingTag          = "gyro";
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        gyro = hardwareMap.get(BNO055IMU.class, "gyro");
        gyro.initialize(parameters);

        jewelServo = hardwareMap.get(Servo.class, "jewelServo");
        armServo = hardwareMap.get(Servo.class, "armServo");
        leftClampServo = hardwareMap.get(Servo.class, "leftClampServo");
        rightClampServo = hardwareMap.get(Servo.class, "rightClampServo");

        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "colorDistanceSensor");
        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "colorDistanceSensor");

        jewelServo.setDirection(Servo.Direction.FORWARD);
        armServo.setDirection(Servo.Direction.FORWARD);
        rightClampServo.setDirection(Servo.Direction.REVERSE);
        leftClampServo.setDirection(Servo.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        /** Start tracking the data sets we care about. */
        relicTrackables.activate();
        runtime.reset();
        double elapsedTime;

        // run until the end of the match (driver presses STOP)
        boolean test = true;
        double speed = .8;
        double jewelDegrees = 10;
        Boolean isDetected = false;
        while (opModeIsActive())
        {
            robot.driveForward(.5,12,true);
            sleep(1000);
            robot.driveForward(.5,-12,true);
            sleep(1000);
            robot.driveForward(-.5,12,true);
            sleep(1000);
            robot.driveForward(-.5,-12,true);
            elapsedTime = runtime.time();
            if (elapsedTime < JEWELCLOSECLAMP)
            {
                clamp(CLOSECLAMPPOSITION);
            }
            else if (elapsedTime < JEWELCHOPTIME)
            {
                jewelServo.setPosition(1);
            }
            else if (elapsedTime < JEWELARMRAISE)
            {
                armServo.setPosition(.9);
            }
            else if (elapsedTime < SPINTOWIN)
            {
                if (isJewelRed()&& !isDetected) {
                    // the red jewel is on the left of sensor
                    speed = -speed;
                    isDetected = !isDetected;
                }
                robot.turnDegrees(speed, jewelDegrees);
            }
            else if (elapsedTime < JEWELSHEATHARM)
            {
                jewelServo.setPosition(.65);
            }
            else if (elapsedTime < JEWELSTOREARM)
            {
                jewelServo.setPosition(0);
            }
            else if (elapsedTime < JEWELSPINBACK)
            {
                robot.turnDegrees(speed, -jewelDegrees);
            }
            else if (elapsedTime < LIFTARM) {
                armServo.setPosition(LIFTEDARMPOSITION);
            }
            else if (elapsedTime < TURNTOLINEUPWITHCOLUMNS)
            {
                robot.turnDegrees(-.5, 150);
            }
            else if(elapsedTime < PHASETHREE)
            {
                int column = this.getColumn(this.trackable);
                double distance = 19.25 + column * 7.5;
                robot.driveForward(.5,distance,true);
            }
            else if(elapsedTime < PHASETHREEHALF)
            {
                robot.turnOffMotors();
            }
            else if (elapsedTime < PHASEFOUR)
            {
                armServo.setPosition(.7);
            }
            else if (elapsedTime < PHASEFIVE)
            {
                robot.turnDegrees(-.5,90);
            }
            else if (elapsedTime < PHASESIX)
            {
                robot.driveForward(.5,12,true);
            }
            else if (elapsedTime < PHASESIXHALF)
            {
                robot.turnOffMotors();
            }
            else if (elapsedTime < PHASESEVEN)
            {
                setClampPosition(OPENCLAMPPOSITION);
            }
            else if (elapsedTime < PHASEEIGHT)
            {
                robot.turnDegrees(.5,15);
            }
            else if (elapsedTime < PHASENINE)
            {
                robot.driveForward(.5,-2,true);
            }
            else if (elapsedTime < PHASENINEHALF)
            {
                robot.turnOffMotors();
            }
            /*else if (elapsedTime < TURNTOWARDSGLYPHPIT)
            {
                turn(1);
            }
            else if (elapsedTime < DRIVETOGLYPHPIT)
            {
                drive(0, 1);
            }
            else if (elapsedTime < GRABABLOCK)
            {
                turnOffMotors();
                setClampPosition(CLOSECLAMPPOSITION);
            }
            else if (elapsedTime < BACKTOBASE)
            {
                drive(0,-1);
            }
            else if (elapsedTime < TURNTOFACECOLUMNS)
            {
                turn(1);
            }
            else {
                turnOffMotors();
            }*/
            updateTelemetry();
        }
    }

    private void initVuforia() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AY5Y5vL/////AAAAGX2e2w6Jc0ethIktT/zzRE1khe+fR9Mt2fiD8nQZ5KNecPTwAiKX5OZSAAZD/AeeaQbXrhx/NUL0ItyuFDzn5tzYDrVFnhryOQMyuK6RZsw0qG60IbzEffXP+ppGpWRvx/Owr+hJJpNcrIo6otnFFZ79vGiQQiDohkAAsHNIXymC8/xgHDk0XXhtU+UYA8yyhzIFOVNgwBRmYmNhomE/wmShZK69EOLfpfRVvjwE8dj2vlhwTChJ1r/4GUyXB7yZ092c19r345QEx511Nhl+Oo3PSolBWO2hn43uRZ5IB4e+cvR/O6KMV25ylM1toRR98TM06NmmGlbR3+19NBA9Ej7T2aOvCf3dSa0ZTpT+haT7";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = parameters.cameraMonitorFeedback.AXES;

        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);

        this.relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        this.trackable = relicTrackables.get(0);
        trackable.setName("RelicRecovery");

    }

    private int getColumn(VuforiaTrackable trackable) {
        int columntoPlaceBlock = -1;
        RelicRecoveryVuMark vumarkImageTracker = RelicRecoveryVuMark.from(trackable);

        if(vumarkImageTracker != RelicRecoveryVuMark.UNKNOWN)
        {
            if(vumarkImageTracker == RelicRecoveryVuMark.LEFT)
            {
                columntoPlaceBlock = 1;
            }else if(vumarkImageTracker == RelicRecoveryVuMark.CENTER)
            {
                columntoPlaceBlock = 2;
            }else if(vumarkImageTracker == RelicRecoveryVuMark.RIGHT)
            {
                columntoPlaceBlock = 3;
            }

            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) trackable.getListener()).getPose();

            if (pose != null) {
                VectorF translation = pose.getTranslation();

                telemetry.addData(trackable.getName() + "-Translation", translation);

                double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));

                telemetry.addData(trackable.getName() + "-degrees", degreesToTurn);

                //find out which array value is x, y, and z
                float zero = translation.get(0);
                float one = translation.get(1);
                float two = translation.get(2);
                telemetry.addData(trackable.getName() + "x", zero);
                telemetry.addData(trackable.getName() + "y", one);
                telemetry.addData(trackable.getName() + "z", two);

            }
        }

        return columntoPlaceBlock;
    }

    public void updateTelemetry(){
        //double armServoValue = armServo.getPosition();
        //double leftClampValue = leftClampServo.getPosition();
        //double rightClampValue = rightClampServo.getPosition();
        //boolean aButton = gamepad1.a;
        telemetry.addData("Status", "Run Time   : " + runtime.toString());

        telemetry.update();
    }

    public double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    //drive method that accepts two values, x and y motion

    private void setClampPosition(double newClampPosition)
    {
        rightClampServo.setPosition(newClampPosition);
        leftClampServo.setPosition(newClampPosition);
    }

    private void closeClamp(double currentPosition)
    {
        double newPosition = currentPosition + .0005;
        rightClampServo.setPosition(newPosition);
        leftClampServo.setPosition(newPosition);
    }

    private  void openClamp(double currentPosition)
    {
        double newPosition = currentPosition - .0005;
        rightClampServo.setPosition(newPosition);
        leftClampServo.setPosition(newPosition);
    }

    private void liftArm(double currentPosition)
    {
        double newPosition = currentPosition - .001;
        armServo.setPosition(newPosition);
    }

    private  void lowerArm(double currentPosition)
    {
        double newPosition = currentPosition + .001;
        armServo.setPosition(newPosition);
    }

    private Boolean isJewelRed()
    {
        double differenceFactor = 1.5;
        double red = sensorColor.red();
        double blue = sensorColor.blue();
        if(red >= 1.5 * blue)
        {
            return true;
        }
        return false;
    }

    public void stop(double seconds) {
        double currentTime = runtime.time();
        double totalTime = currentTime + seconds;

        while(currentTime < totalTime) {
            currentTime = runtime.time();
        }
    }

    // takes in a position from 0 (open all the way) to .5 (closed)
    private void clamp(double position)
    {
        rightClampServo.setPosition(position);
        leftClampServo.setPosition(position);
    }

}
