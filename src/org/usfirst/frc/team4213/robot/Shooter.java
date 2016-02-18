package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.CANSpeedController.ControlMode;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;

public class Shooter {
	protected CANTalon motorPitch;
	protected CANTalon motorYaw;
	protected CANTalon motorFlywheel;
	protected CANTalon motorCam;
	
	protected Encoder camEncoder;
	protected Encoder stringPot;
	
	final static int GEARRATIO1 = 3;
	final static int GEARRATIO2 = 188;
	final static int PPR = 7;
	double gearRatio = GEARRATIO1 * GEARRATIO2 * PPR;
	
	public Shooter (CANTalon motorPitch, CANTalon motorYaw, CANTalon motorFlywheel, CANTalon motorCam, Encoder camEncoder, Encoder stringPot) {
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
		stringPot.reset();
		
		
		
	}
	
	public Shooter(){
		// test case constructor 
		
		this.motorPitch = new CANTalon(3);
		DriverStation.reportError(""+motorPitch.getAnalogInPosition(),false);
		motorPitch.setControlMode(TalonControlMode.Position.value);
		DriverStation.reportError(""+motorPitch.getControlMode(),false);
		//motorPitch.setEncPosition(0);
		printAngle();
		motorPitch.enable();
		motorPitch.reset();
		printAngle();
		motorPitch.setFeedbackDevice(FeedbackDevice.QuadEncoder);

		
		

		motorPitch.setPID(5, 0, 0);
		motorPitch.enableControl();
	}
	
	public void angleSet (double angle) {
		motorPitch.set((angle/360)*(gearRatio));  ///multiply by 4
		printAngle();
	}
	
	public void angleIncrease(){
		if(motorPitch.get()<200000){  //MAX_ANGLE
			motorPitch.set(motorPitch.get()+100);
		}
		printAngle();
	}
	
	public void angleDecrease(){
		if(motorPitch.get()>0){  //MAX_ANGLE
			motorPitch.set(motorPitch.get()+100);
		}
		printAngle();
	}
	
	private void printAngle(){
		DriverStation.reportError("        Angle:"+( (motorPitch.getEncPosition() / gearRatio ) * 360),false);
				
	}
	
	
	
}
