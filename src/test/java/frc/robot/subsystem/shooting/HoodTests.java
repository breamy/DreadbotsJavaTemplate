package frc.robot.subsystem.shooting;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.subsystem.shooting.Hood;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HoodTests {
    //public Hood(CANSparkMax motor, DigitalInput upperSwitch, DigitalInput lowerSwitch)
    private CANSparkMax motor = mock(CANSparkMax.class);
    private DigitalInput upperSwitch = mock(DigitalInput.class);
    private DigitalInput lowerSwitch = mock(DigitalInput.class);
    private CANPIDController pidController = mock(CANPIDController.class);
    private CANEncoder encoder = mock(CANEncoder.class);

    private Hood hood;

    @BeforeEach // this method will run before each test
    public void setup() {
        assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
        when(motor.getPIDController()).thenReturn(pidController);
        when(motor.getEncoder()).thenReturn(encoder);
        hood = new Hood(motor, upperSwitch, lowerSwitch);
    }

    @Test
    public void testHoodState() {
        // true = limit not reached, false = limit reached
        when(lowerSwitch.get()).thenReturn(true, true, true, false);
        when(upperSwitch.get()).thenReturn(true, true, true, false);
        when(encoder.getPosition()).thenReturn(300.0,150.0, 225.0);

        // Not calibrated yet, ready should be false
        assertEquals(false, hood.isReady());
        hood.startCalibration();
        // Calibration still running, ready should be false
        assertEquals(false, hood.isReady());

        hood.setHoodPosition(.8);
        // Calibration is still running, so setHoodPosition doesn't do anything, state should still be Calibrating
        assertEquals(Hood.States.CALIBRATING, hood.state);

        // lowerSwitch.get() is sometimes called more than once per calibrate() call
        // looks like 4 iterations is what it takes to get both switches to be hit
        for (int i = 0; i < 4; i++) {
            hood.calibrate();
            assertEquals(false, hood.isReady());
        }
        hood.calibrate();
        // Calibration finished, hood is ready
        assertEquals(true, hood.isReady());

        when(encoder.getPosition()).thenReturn(222.0, 180.0);
        hood.setHoodPosition(.8);
        // with the mock, it takes 2 iterations to get to the target position
        // should be ADJUSTING after the first iteration
        assertEquals(Hood.States.ADJUSTING, hood.state);
        assertFalse(hood.isReady());

        hood.setHoodPosition(.8);
        // at target position, ready to shoot.
        assertTrue(hood.isReady());
        assertEquals(Hood.States.READY, hood.state);

        // Lets try it where it takes 3 iterations to get to target position:
        when(encoder.getPosition()).thenReturn(222.0, 180.0, 285.0);
        hood.setHoodPosition(0.1);
        // should be ADJUSTING after the first iteration
        assertFalse(hood.isReady());
        assertEquals(Hood.States.ADJUSTING, hood.state);

        hood.setHoodPosition(0.1);
        // haven't gotten to target position yet, still ADJUSTING
        assertFalse(hood.isReady());
        assertEquals(Hood.States.ADJUSTING, hood.state);

        hood.setHoodPosition(0.1);
        // at target position, ready to shoot.
        assertTrue(hood.isReady());
        assertEquals(Hood.States.READY, hood.state);
    }

}