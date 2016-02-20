package org.usfirst.frc.team4213.robot;

import java.util.Dictionary;

import org.team4213.lib14.EncMotorTalon;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class Turret {
	
	protected CANTalon motorPitch;
	protected CANTalon motorYaw;
	protected CANTalon motorFlywheel;
	protected CANTalon motorCam;
	protected Potentiometer stringPot;

	public enum TurretState {
		DOWN,RAISED,ARMED;
	}
	
	private TurretState state = TurretState.DOWN;
	
	final static int PITCH_GEARRATIO1 = 3;
	final static int PITCH_GEARRATIO2 = 188;
	final static int PITCH_PPR = 7;
	final static int PITCH_OUT_PPR = PITCH_GEARRATIO1 * PITCH_GEARRATIO2 * PITCH_PPR;
	final static int STARTING_ANGLE = 0;
	final static int MID = 38;
	final static int PITCH_MIN_ANGLE = 20 + MID;
	final static int PITCH_MAX_ANGLE = 85 + MID;
	
	
	/*public  Turret(CANTalon motorPitch, CANTalon motorYaw, CANTalon motorFlywheel, CANTalon motorCam, Encoder camEncoder, Encoder stringPot) {
		motors.get    = motorPitch;
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
		
		
		
	}*/
	
	public Turret(){
		// test case constructor 
		motorPitch =  new CANTalon(3);
		motorFlywheel =  new CANTalon(4);
		motorCam = new EncMotorTalon(0, new Encoder(4, 5, false, CounterBase.EncodingType.k1X));
		motorYaw = new CANTalon(1);
		stringPot =	new AnalogPotentiometer(0, 1000 , 0);

		
		//DriverStation.reportError(""+motorPitch.getAnalogInPosition(),false);
		motorPitch.setControlMode(TalonControlMode.Position.value);
		//DriverStation.reportError(""+motorPitch.getControlMode(),false);
		//motorPitch.setEncPosition(0);
		printAngle();
		motorPitch.enable();
		motorPitch.reset();
		printAngle();
		motorPitch.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		motorPitch.setPID(3, 0, 0);
		motorPitch.enableControl();
		DriverStation.reportError("\n count to set" + (int)PITCH_OUT_PPR*STARTING_ANGLE/360, false);
		motorPitch.setEncPosition((int)PITCH_OUT_PPR*STARTING_ANGLE/360);
		motorPitch.setPosition((int)PITCH_OUT_PPR*STARTING_ANGLE/360);
		
		
		
	}
	
	public void angleSet (double angle) {
		motorPitch.set((angle/360)*(PITCH_OUT_PPR));  ///multiply by 4
		printAngle();
	}
	
	public void angleIncrease(){
		//if(motorPitch.get()){  //MAX_ANGLE
		motorPitch.setControlMode(TalonControlMode.PercentVbus.value);
		motorPitch.set(motorPitch.get()+100);
		
		//}
		printAngle();
	}
	
	public void turnTurret(double angle){
		
	}
	
	public void turnTurretCW(){
		if(state == TurretState.RAISED){ // And Prevent Further Turns
			motorYaw.set(1);
		}
	}
	
	public void turnTurretCCW(){
		if(state == TurretState.RAISED){ // And Prevent Further Turns
			motorYaw.set(-1);
		}
	}
	
	
	public void angleDecrease(){
		//if(motorPitch.get()){  //MAX_ANGLE
		motorPitch.set(motorPitch.get()-100);
		//}
		printAngle();
	}
	
	private void printAngle(){
		DriverStation.reportError("        Angle:"+( (motorPitch.getEncPosition() / PITCH_OUT_PPR ) * 360),false);
	}
	
	public void raiseTurret(){
		state = TurretState.RAISED;
		motorPitch.set(PITCH_OUT_PPR*(25/360));
	}
	
	public void dropTurret(){
		state = TurretState.DOWN;
		motorPitch.set(PITCH_OUT_PPR*(STARTING_ANGLE/360));
	}
	
	public void armShooter(){
		
	}
	
	public int getAngle(){
		return motorPitch.getEncPosition();
	}
	
	public void stop(){
		motorPitch.changeControlMode(TalonControlMode.PercentVbus);
		motorPitch.set(0);
		motorPitch.changeControlMode(TalonControlMode.Position);
		
	}

}
