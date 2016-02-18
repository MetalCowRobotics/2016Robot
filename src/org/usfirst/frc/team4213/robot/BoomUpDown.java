package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class BoomUpDown {
	Encoder enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	CANTalon boomMotor = new CANTalon(3);
	
	// Sets what angle the boom moves to.
	public static void boomRegMove(int setPoint) {
		
	}
	public void boomMove(){
	
		
	}//end function boomMovement
	

	
}//end class BoomUpDown
