package org.team4213.lib14;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class EncMotorTalon extends CANTalon{
	
	int encCount;
	int lastEncCount;
	Encoder encoder;

	public EncMotorTalon(int deviceNumber, Encoder encoder){
		super(deviceNumber);
		this.encoder = encoder;
		encoder.reset();
		
		super.setControlMode(TalonControlMode.Position.value);
		super.setPID(3, 0, 0);
		super.enableControl();
		
		encCount = getEncPosition();
	}
	
	public int getEncPosition(){
		return encoder.get();
	}
	
	public void setEncPosition(int count){
		lastEncCount = count;
		encCount = count;
		update();
	}
	
	public void update(){
		encCount += encoder.get()-lastEncCount;
		lastEncCount = encoder.get();
		super.setEncPosition(encCount);
	}
		
}
