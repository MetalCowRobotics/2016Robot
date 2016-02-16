package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class Shooter {
	protected CANTalon motorPitch;
	protected CANTalon motorYaw;
	protected CANTalon motorFlywheel;
	protected CANTalon motorCam;
	
	protected Encoder camEncoder;
	protected Encoder yawEncoder;
	
	public Shooter(CANTalon motorPitch, CANTalon motorYaw, CANTalon motorFlywheel, CANTalon motorCam, Encoder camEncoder, Encoder yawEncoder){
		this.motorPitch = motorPitch;
		this.motorYaw = motorYaw;
		this.motorFlywheel = motorFlywheel;
		this.motorCam = motorCam;
		
		this.camEncoder = camEncoder;
		this.yawEncoder = yawEncoder;
	}
	
	public void tiltUp(double angle){
		
		// Figure out Encoders ... I have No IDEA
		motorPitch.setPosition(angle);
	}
	

}
