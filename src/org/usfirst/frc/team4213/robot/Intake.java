package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANSpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake{
	
	double setpoint;
	Encoder enc;
	CANSpeedController intakeMotor;
	
	public Intake(final int channel) {
		enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
		intakeMotor = new CANTalon(channel);
		DriverStation.reportError("("+enc.getDistance()+")", false);
	}
	
	public void intake(double speed){
		intakeMotor.set(speed);
	}
	
	public void intakeStop(){
		intakeMotor.set(0);
	}
	

}
