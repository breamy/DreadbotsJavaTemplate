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

//	public Intake() {
//		this.intakeMotor = new CANSparkMax(Constants.INTAKE_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
//		//this.pidController = intakeMotor.getPIDController();
//		this.intakePin = new Solenoid(Constants.INTAKE_PIN_ID);
//
//		this.pidController.setP(6e-5);
//		this.pidController.setI(1e-6);
//		this.pidController.setD(0.3);
//		this.pidController.setIZone(0);
//		this.pidController.setFF(.000015);
//		this.pidController.setOutputRange(-1.0, 1.0);
//	}
//
//	public void setSpeed(double speed) {
//		pidController.setReference(speed, ControlType.kVelocity);
//	}
//
//	public void setPercentOutput(double percentOutput) {
//		intakeMotor.set(percentOutput);
//	}
//
//	public CANSparkMax getIntakeMotor() {
//		return intakeMotor;
//	}
//
//	public CANPIDController getPidController() {
//		return pidController;
//	}
//
//	public Solenoid getIntakePin() {
//		return intakePin;
//	}
}
