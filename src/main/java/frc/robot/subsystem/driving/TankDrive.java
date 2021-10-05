package frc.robot.subsystem.driving;

import edu.wpi.first.wpilibj.SpeedController;
import frc.robot.subsystem.SubsystemBase;
import frc.robot.utility.DreadbotMath;

import java.util.ArrayList;

public class TankDrive extends SubsystemBase implements Drive {

	private final SpeedController leftFrontMotor;
	private final SpeedController leftRearMotor;
	private final SpeedController rightFrontMotor;
	private final SpeedController rightRearMotor;
	private final ArrayList<SpeedController> allMotors;

	private final double DEAD_ZONE = 0.2;

	public TankDrive(SpeedController leftFront, SpeedController rightFront,
					 SpeedController leftRear, SpeedController rightRear) {
		super("SparkDrive");

		leftFrontMotor = leftFront;
		leftRearMotor = leftRear;
		rightFrontMotor = rightFront;
		rightRearMotor = rightRear;
		allMotors = new ArrayList<>();
		allMotors.add(leftFront);
		allMotors.add(rightFront);
		allMotors.add(leftRear);
		allMotors.add(rightRear);

		this.stop();

		configureTests();
	}

	private void configureTests() {
		// Runs a forward drive test for 2.5s, then stops the drivetrain.
		addTest(() -> drive(1.0d, 0.0d, DriveMode.TURTLE, 0.0d), "Forward Drive Test", 2.5d);
		addTest(this::stop, 1.0d);

		// Runs a backward drive test for 2.5s, then stops the drivetrain.
		addTest(() -> drive(-1.0d, 0.0d, DriveMode.TURTLE, 0.0d), "Backward Drive Test", 2.5d);
		addTest(this::stop, 1.0d);

		// Runs a rotation (positive) drive test for 2.5s, then stops the drivetrain.
		addTest(() -> drive(0.0d, 1.0d, DriveMode.TURTLE, 0.0d), "Positive Rotation Drive Test", 2.5d);
		addTest(this::stop, 1.0d);

		// Runs a rotation (negative) drive test for 2.5s, then stops the drivetrain.
		addTest(() -> drive(0.0d, -1.0d, DriveMode.TURTLE, 0.0d), "Negative Rotation Drive Test", 2.5d);
		addTest(this::stop, 1.0d);
	}

	/**
	 * Stops all motors of the drivetrain.
	 */
	@Override
	public void stop() {
		for (SpeedController motor: allMotors) {
			motor.set(0.0d);
		}
	}

	/**
	 * An improved and more readable version of the Dreadbot's homemade tank
	 * drive function with default values for final value multiplier and joystick
	 * deadband.
	 *
	 * @param forwardAxisFactor  The forward factor of the drivetrain control.
	 * @param rotationAxisFactor The rotational factor of the drivetrain control.
	 */
	@Override
	public void drive(double forwardAxisFactor,
	                      double rotationAxisFactor) {
		drive(forwardAxisFactor, rotationAxisFactor, DriveMode.NORMAL);
	}

	/**
	 * An improved and more readable version of the Dreadbot's homemade tank
	 * drive function with default values for the joystick deadband.
	 *
	 * @param forwardAxisFactor    The forward factor of the drivetrain control.
	 * @param rotationAxisFactor   The rotational factor of the drivetrain control.
	 * @param driveMode 		   Drivemode to use (Slow, Regular, Turbo)
	 */
	@Override
	public void drive(double forwardAxisFactor,
	                      double rotationAxisFactor,
	                      final DriveMode driveMode) {
		drive(forwardAxisFactor, rotationAxisFactor, driveMode, DEAD_ZONE);
	}

	/**
	 * An improved and more readable version of the Dreadbot's homemade tank
	 * drive function.
	 *
	 * @param forwardAxisFactor    The forward factor of the drivetrain control.
	 * @param rotationAxisFactor   The rotational factor of the drivetrain control.
	 * @param driveMode 		   Drivemode to use (Slow, Regular, Turbo)
	 * @param joystickDeadband     The applied joystick deadband.
	 */
	public void drive(double forwardAxisFactor,
	                      double rotationAxisFactor,
	                      final DriveMode driveMode,
	                      final double joystickDeadband) {

		// Clamp Values to Acceptable Ranges (between -1.0 and 1.0).
		forwardAxisFactor = DreadbotMath.clampValue(forwardAxisFactor, -1.0d, 1.0d);
		rotationAxisFactor = DreadbotMath.clampValue(rotationAxisFactor, -1.0d, 1.0d);

		// Apply an Optional Joystick Deadband
		forwardAxisFactor = DreadbotMath.applyDeadbandToValue(forwardAxisFactor, -joystickDeadband, joystickDeadband, 0.0d);
		rotationAxisFactor = DreadbotMath.applyDeadbandToValue(rotationAxisFactor, -joystickDeadband, joystickDeadband, 0.0d);

		// Essential Drive Math based on the two movement factors.
		double leftFinalSpeed = (-forwardAxisFactor + rotationAxisFactor) * driveMode.finalValueMultiplier;
		double rightFinalSpeed = (forwardAxisFactor + rotationAxisFactor) * driveMode.finalValueMultiplier;

		// Normalize the values to become between 1.0 and -1.0.
		double normalizeDivisor = DreadbotMath.getNormalizationDivisor(leftFinalSpeed, rightFinalSpeed);

		leftFinalSpeed = leftFinalSpeed / normalizeDivisor;
		rightFinalSpeed = rightFinalSpeed / normalizeDivisor;
		System.out.println("FinalSpeed Left: " + leftFinalSpeed + " Right: " + rightFinalSpeed);
		// Assign each motor value to the output side it's on.
		leftFrontMotor.set(leftFinalSpeed);
		leftRearMotor.set(leftFinalSpeed);
		rightFrontMotor.set(rightFinalSpeed);
		rightRearMotor.set(rightFinalSpeed);
	}
}
