package org.team4213.lib14;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import edu.wpi.first.wpilibj.DriverStation;

public class CowCamController<T>{
	
	
	
	private VideoCapture videoCapture = new VideoCapture();;
    volatile private Mat camImage = new Mat();
    // Single Threaded Async Executor
    private ExecutorService executor = Executors.newWorkStealingPool();;
    // Camera Server Instance
    private Optional<CowCamServer> camServer;
    // Future for the Executed Image Processing
    private Optional<Future<T>> processFuture;
    // Output from the Image Process Task
    volatile private Optional<T> outputData;
    // Task Given to Execute
    private Optional<Callable<T>> task;
    
	
	public CowCamController(int cameraPort, int fps, boolean streamCamera, Optional<Callable<T>> imgProcess){
		
		DriverStation.reportError("fancy", false);
		
		// Attempts to Open the Camera
		while ( true ){
    		try {
    			videoCapture.open(cameraPort,320,240, fps);
    		}catch ( Exception ex ){
    			continue;
    		}
    		break;
    	}
		
		// If the Stream Camera boolean is True, Initialize a new Camera Server
		if(streamCamera){
			camServer = Optional.of(new CowCamServer(this,1180));
		}
		
		// Set the Camera's Settings
		setCameraOptions();
		
		task = imgProcess;
		
		// Reads first Image
		videoCapture.read(camImage);
		
		// Executes the Task Once
		processFuture = task.isPresent() ? Optional.of(executor.submit(task.get())) : Optional.empty();
		
	}
	
	
	/*
	 * Sets the Camera's Settings
	 */
	private void setCameraOptions(){
    	videoCapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH ,320);
    	videoCapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT , 240);
	}
	
	public Mat getCamImage(){
		return camImage;
	}
	
	
	/*
	 * Public function to Start the Infinite Camera Loop + Processing Task
	 */
	public void start() {
		
		// Checks if We use a Camera Server
		if(camServer.isPresent()){
			// If so , a new async process is created that runs the server loop
			executor.submit(camServer.get());
		}
		// Then We run the Infinite Camera Read Loop
		executor.submit(readCamera());
		
	}
	
	/*
	 * Private for creating the Infinite Camera Loop ( Blocking ) 
	 */
	private Runnable readCamera(){
		// Returns a Runnable Instance and Returns it
		return ()->{
			while(true){
				// Reads Camera to an Image Variable
				videoCapture.read(camImage);
				// Submits Task to Run Async + Get its future if The Process Finished.
				if(task.isPresent() && processFuture.isPresent() && processFuture.get().isDone()){
						// Gets Data from the Future
						outputData = Optional.of(processFuture.get().get());
						// Replaces the Old Future with a New One
						processFuture = Optional.of(executor.submit(task.get()));
				}
			}
		};
	}
	
	
}
