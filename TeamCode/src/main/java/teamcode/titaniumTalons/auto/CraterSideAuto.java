package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Drive;

@Autonomous(name = "CraterSideAuto", group = "Linear OpMode")
public class CraterSideAuto extends AbstractAuto {

    @Override
    protected void driveToDepot() {
        switch (goldLocation) {
            case LEFT:
                goldLeftDriveToDepot();
                break;
            case MIDDLE:
                goldMiddleDriveToDepot();
                break;
            case RIGHT:
                goldRightDriveToDepot();
                break;
        }
        releaseMarker();
    }

    private void goldLeftDriveToDepot() {
        Drive.turnDefinite(30, 1.0);
        Drive.driveLateralDefinite(-20.0, 1.0);
        Drive.turnDefinite(-135.0, 1.0);
        Drive.driveLateralDefinite(8.0, 1.0);
        Drive.driveVerticalDefinite(48.0, 1.0);
    }

    private void goldMiddleDriveToDepot() {
        Drive.driveVerticalDefinite(-4.0, 1.0);
        Drive.driveLateralDefinite(-40.0, 1.0);
        Drive.turnDefinite(-135.0, 1.0);
        Drive.driveLateralDefinite(14.0, 1.0);
        Drive.driveVerticalDefinite(40.0, 1.0);
    }

    private void goldRightDriveToDepot() {
        Drive.turnDefinite(-30.0, 1.0);
        Drive.driveVerticalDefinite(-4.0, 1.0);
        Drive.driveLateralDefinite(-60.0, 0.8);
        Drive.turnDefinite(-135.0, 1.0);
        Drive.driveLateralDefinite(14.0, 1.0);
        Drive.driveVerticalDefinite(39.0, 1.0);
    }

}
