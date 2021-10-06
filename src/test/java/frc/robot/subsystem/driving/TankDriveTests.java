package frc.robot.subsystem.driving;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.SpeedController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TankDriveTests {
    SpeedController leftFront = mock(SpeedController.class);
    SpeedController rightFront = mock(SpeedController.class);
    SpeedController leftRear = mock(SpeedController.class);
    SpeedController rightRear = mock(SpeedController.class);

    @BeforeEach // this method will run before each test
    public void setup() {
        assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
  //      leftFront = new SpeedControllerSim(1); // create our simulation PWM motor controller
  //      rightFront = new PWMSim(2); // create our simulation PWM motor controller
  //      leftRear = new PWMSim(3); // create our simulation PWM motor controller
  //      rightRear = new PWMSim(4); // create our simulation PWM motor controller
  //      simPiston = new DoubleSolenoidSim(IntakeConstants.PISTON_FWD, IntakeConstants.PISTON_REV); // create our simulation solenoid
    }

//    @After // this method will run after each test
//    public void shutdown() throws Exception {
//        intake.close(); // destroy our intake object
//    }
    @Test
    public void testStop() {
        Drive tankDrive = new TankDrive(leftFront, rightFront, leftRear, rightRear);
        Mockito.reset(leftFront, rightFront, leftRear, rightRear);

        tankDrive.stop();
        verify(leftFront).set(0.0);
        verify(rightFront).set(0.0);
        verify(leftRear).set(0.0);
        verify(rightRear).set(0.0);
    }

    @Test
    public void testDrive() {
        Drive tankDrive = new TankDrive(leftFront, rightFront, leftRear, rightRear);
        Mockito.reset(leftFront, rightFront, leftRear, rightRear);

        tankDrive.drive(1, 1);
        verify(leftFront).set(0.0);
        verify(rightFront).set(1.0);
        verify(leftRear).set(0.0);
        verify(rightRear).set(1.0);

        Mockito.reset(leftFront, rightFront, leftRear, rightRear);
        tankDrive.drive(1.0, 0.0);
        verify(leftFront).set(-0.5);
        verify(rightFront).set(0.5);
        verify(leftRear).set(-0.5);
        verify(rightRear).set(0.5);

        Mockito.reset(leftFront, rightFront, leftRear, rightRear);
        tankDrive.drive(0.5, -0.5);
        verify(leftFront).set(-0.5);
        verify(rightFront).set(0.0);
        verify(leftRear).set(-0.5);
        verify(rightRear).set(0.0);
    }
    @Test
    public void testDriveDeadzone() {
        Drive tankDrive = new TankDrive(leftFront, rightFront, leftRear, rightRear);
        Mockito.reset(leftFront, rightFront, leftRear, rightRear);

        tankDrive.drive(.19, .19);
        verify(leftFront).set(0.0);
        verify(rightFront).set(0.0);
        verify(leftRear).set(0.0);
        verify(rightRear).set(0.0);

        Mockito.reset(leftFront, rightFront, leftRear, rightRear);
        tankDrive.drive(.2, .2);
        verify(leftFront).set(0.0);
        verify(rightFront).set(0.2);
        verify(leftRear).set(0.0);
        verify(rightRear).set(0.2);

        Mockito.reset(leftFront, rightFront, leftRear, rightRear);
        tankDrive.drive(.22, .21);
        verify(leftFront).set(-0.0050000000000000044);
        verify(rightFront).set(0.215);
        verify(leftRear).set(-0.0050000000000000044);
        verify(rightRear).set(0.215);
    }
}