package org.team4213.lib14;

// Import Various Java Network Classes
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

// Import OpenCV Classes
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;

// Import WPILib's Driver Station Item
import edu.wpi.first.wpilibj.DriverStation;

public class CowCamServer implements Runnable{
	
	private CowCamController cameraController;

	ServerSocket socket;
	
	private static final byte[] kMagicNumber = {0x01, 0x00, 0x00, 0x00};
	
	// Initializer for Camera Server . Takes a Camera Controller and Server Port
	public CowCamServer(CowCamController controller, int port){
		cameraController = controller;
		setupSocket(port);
	}
	
	// Creates a Socket for the Camera Server on the Desired port ( 1180 )
	private void setupSocket(int port){
		try{
		socket = new ServerSocket();
		socket.setReuseAddress(true);
	    InetSocketAddress address = new InetSocketAddress(port);
	    socket.bind(address);
	    }catch(IOException ex){
	    	DriverStation.reportError("Socket Failed", true);
	    }
	}
	
	
	// Converts a Mat to a Byte Array
	public byte[] matToByteArray(Mat camImage){
		
		// Sets JPEG Quality to 10*50
        MatOfInt params = new MatOfInt(Highgui.IMWRITE_JPEG_QUALITY, 10*50);

        // Creates a Mat of Bytes
		MatOfByte matByte = new MatOfByte();
		
		// Encoded the Image into JPEG and Stores it in the Mat of Bytes
		Highgui.imencode(".jpg", camImage, matByte, params);
	  
		// Returns the Mat of Bytes as an Array
		return matByte.toArray();
	}
	
	@Override
	public void run() {
    	
		// Runs an Infinite Loop for the Server
        while (true) {
        	try {
        		// Starts Socket + Waits for Communication
	            Socket s = socket.accept();
	            
	            // Acquires the Data Streams
	            DataInputStream is  = new DataInputStream(s.getInputStream());
	            DataOutputStream os = new DataOutputStream(s.getOutputStream());
	            
	            // Reads the Data Input Stream
	            int fps         = is.readInt();
	            int compression = is.readInt();
	            
	            // Resolution Setting that Could be Implemented Later
	            //int size        = is.readInt();
	            
	            
	            // Checks if Dashboard is Set to HW Camera and Alerts Driver if Not
	            if (compression != -1) {
	              DriverStation.reportError("Choose \"USB Camera HW\" on the dashboard", false);
	              s.close();
	              continue;
	            }
	
	            // Wait for the camera
	            long period = (long) (1000 / (1.0 * fps));
	        	while (true) {
	        		long t0 = System.currentTimeMillis();
	        		byte[] videoBits = matToByteArray(cameraController.getCamImage());
	
	        		// Streams data to Client
	        		try {
	        			os.write(kMagicNumber);
		                os.writeInt(videoBits.length);
		                os.write(videoBits);
		                os.flush();
		                long dt = System.currentTimeMillis() - t0;
		                
		                // Sleeps to Delay for FPS Setting
		                if (dt < period) {
		                	Thread.sleep(period - dt);
		                }
		            
	        		} catch (IOException | UnsupportedOperationException ex) {
	        			DriverStation.reportError(ex.getMessage(), true);
	        			break;
	        		}             
	        	}
            
          	} catch (IOException ex) {
          		DriverStation.reportError(ex.getMessage(), true);
          		continue;
          	}
          
        }
    }
}
