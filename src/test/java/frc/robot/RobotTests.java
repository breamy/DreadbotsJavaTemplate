package frc.robot;

import frc.robot.subsystem.driving.Drive;
import frc.robot.subsystem.driving.DriveMode;
import frc.robot.utility.DreadbotController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class RobotTests {
    private Robot robot;

    @BeforeEach
    public void setup() {
        robot = new Robot();
        robot.robotInit();
    }

    @Test
    public void testTeliopPeriodic() {
        DreadbotController controller = mock(DreadbotController.class);
        Drive drive = mock(Drive.class);
        when(controller.getYAxis()).thenReturn(.5d);
        when(controller.getZAxis()).thenReturn(-0.75d);
        robot.setPrimaryJoystick(controller);
        robot.setDrive(drive);

        robot.teleopPeriodic();
        verify(drive).drive(.5d, -0.75d, DriveMode.NORMAL);

        reset(drive);
        when(controller.isRightTriggerPressed()).thenReturn(true);
        robot.teleopPeriodic();
        verify(drive).drive(.5d, -0.75d, DriveMode.TURBO);

        reset(drive);
        when(controller.isRightTriggerPressed()).thenReturn(false);
        when(controller.isRightBumperPressed()).thenReturn(true);
        robot.teleopPeriodic();
        verify(drive).drive(.5d, -0.75d, DriveMode.TURTLE);
    }
}
