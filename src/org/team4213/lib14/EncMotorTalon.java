package org.team4213.lib14;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class EncMotorTalon extends CANTalon{
	
	int encCount;
	int lastEncCount;
	Encoder encoder;

	public EncMotorTalon(int deviceNumber, Encoder encoder){
		super(deviceNumber);
		this.encoder = encoder;
		encoder.reset();
		encCount = getEncPosition();
	}
	
	public int getEncPosition(){
		return encoder.get();
	}
	
	public void setEncPosition(int count){
		encCount = count;
		update();
	}
	
	public void update(){
		encCount += encoder.get()-lastEncCount;
		lastEncCount = encoder.get();
		super.setEncPosition(encCount);
	}
		
}
