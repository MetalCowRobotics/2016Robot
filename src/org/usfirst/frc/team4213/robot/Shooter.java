package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class Shooter {
	protected CANTalon motorPitch;
	protected CANTalon motorYaw;
	protected CANTalon motorFlywheel;
	protected CANTalon motorCam;
	
	protected Encoder camEncoder;
	protected Potentiometer stringPot;
	
	final static int GEARRATIO1 = 3;
	final static int GEARRATIO2 = 188;
	final static int PPR = 7;
	
	public Shooter (CANTalon motorPitch, CANTalon motorYaw, CANTalon motorFlywheel, CANTalon motorCam, Encoder camEncoder, Potentiometer stringPot) {
		this.motorPitch    = motorPitch;
		this.motorYaw      = motorYaw;
		this.motorFlywheel = motorFlywheel;
		this.motorCam      = motorCam;
		
		this.camEncoder = camEncoder;
		this.stringPot = stringPot;
		
		// Connected to Talon ( Pitch ) 
		// Connected to Climber ( Mecanizm ) 
		
		// I have a quick question, no args constructor '' WhAT IS +THAT ? 
		motorPitch.setEncPosition(0);
		camEncoder.reset();
		
	}
	

	// Temporary Constructor for Testing Purposes
	public Shooter() {
		AnalogInput input = new AnalogInput(9);
		stringPot = new AnalogPotentiometer(input, 360,0);
		motorYaw = new CANTalon(1);
	}


	public double getPotReading(){
		return stringPot.get();
	}
	
	public void setYawSpeed(double speed){
		motorYaw.set(speed);
	}
	
	
	/*public void tiltUp (double angle) {
		
		motorPitch.setPosition((angle/360)/(GEARRATIO1 * GEARRATIO2 * PPR));
	}*/
}
