package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake{
	
	double setpoint;
	Encoder enc;
	CANTalon intakeMotor;
	
	public Intake(CANTalon intakeMotorController) {
		intakeMotor = intakeMotorController;
	}
	
	public void intake(double speed){
		intakeMotor.set(speed);
	}
	
	public void intakeStop(){
		intakeMotor.set(0);
	}
	

}
