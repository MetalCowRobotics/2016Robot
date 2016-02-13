package org.usfirst.frc.team4213.robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CANSpeedController;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Skis{
	Encoder encoder;
	private CANSpeedController skisMotor;	
	
	
	private final double SKIS_MIN = 0;
	private final double SKIS_MAX = 1;
	
	
	public Skis(final int channel){
		skisMotor = new CANTalon(channel);
		encoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	}
	
	
	public void setSkisUp(double speed){
		if(encoder.getDistance() > SKIS_MIN || encoder.getDistance() < SKIS_MAX){
			skisMotor.set(speed);
		}
	}
	
	public void setSkisDown(double speed){
		if(encoder.getDistance() > SKIS_MIN || encoder.getDistance() < SKIS_MAX){
			skisMotor.set(-speed);
		}		
	}
	
	
}
