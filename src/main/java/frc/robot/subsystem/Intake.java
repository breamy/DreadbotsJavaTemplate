package frc.robot.subsystem;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import frc.robot.utility.Constants;

public class Intake extends MotorSafety {
	private static final double DEFAULT_SPEED = 0.5;

	private SpeedController intakeMotor;
	private Solenoid intakePin;

	public Intake(SpeedController motor, Solenoid intakePin) {
		this.intakeMotor = motor;
		this.intakePin = intakePin;
	}

	public void deployIntake() {
		//this is true because true flips the solenoid from what is default,
		//and the pin should physically be setup so that it is extended by default
		intakePin.set(true);
	}

	public void start() {
		intakeMotor.set(DEFAULT_SPEED);
		feed();
	}

	public void stop() {
		intakeMotor.set(0.0d);
		feed();
	}

	public void reverse() {
		intakeMotor.set(-DEFAULT_SPEED);
		feed();
	}

	@Override
	public void stopMotor() {
		stop();
	}

	@Override
	public String getDescription() {
		return "Motor to pickup balls froom the field";
	}
}
