package org.team4213.lib14;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import edu.wpi.first.wpilibj.DriverStation;

public class CowCamController<T> {
	
	
	
	private VideoCapture videoCapture = new VideoCapture();;
    public Mat camImage = new Mat();
    // Single Threaded Async Executor
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    // Camera Server Instance
    private Optional<CowCamServer> camServer;
    private Optional<Future<T>> processFuture;
    public Optional<T> outputData;
    
	
	public CowCamController(int cameraPort, int fps, boolean streamCamera){
		
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
		
	}
	
	
	/*
	 * Sets the Camera's Settings
	 */
	private void setCameraOptions(){
    	videoCapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH ,320);
    	videoCapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT , 240);
	}
	
	/*
	 * Public function to Start the Infinite Camera Loop + Processing Task
	 */
	public void start(Optional<Callable<T>> task){
		try{
			runCamera(task);
		}catch(Exception ex){
			DriverStation.reportError(ex.getMessage(), true);
		}
	}
	/*
	 * Private function to Run the Infinite Camera Loop ( Blocking ) 
	 */
	private void runCamera(Optional<Callable<T>> task) throws InterruptedException, ExecutionException {
		// Reads the First Camera Image so the Camera Server has something to Display
		videoCapture.read(camImage);
		// Executes the First Task so it's Initialized
		processFuture = task.isPresent() ? Optional.of(executor.submit(task.get())) : Optional.empty();
		// Checks if We use a Camera Server
		if(camServer.isPresent()){
			// If so , a new async process is created that runs the server loop
			executor.submit(()->{
				try {
					camServer.get().runServer();
				} catch (Exception e) {
					DriverStation.reportError(e.getMessage(), true);
				}
			});
		}
		// Then We run the Infinite Camera Read Loop
		while(true){
			// Reads Camera to an Image Variable
			videoCapture.read(camImage);
			// Submits Task to Run Async + Get its future if The Process Finished.
			if(task.isPresent() && processFuture.isPresent() && processFuture.get().isDone()){
					outputData = Optional.of(processFuture.get().get());
				
				processFuture = Optional.of(executor.submit(task.get()));
			}
		}
	}
	
	
}
