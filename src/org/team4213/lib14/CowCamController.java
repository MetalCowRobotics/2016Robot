package org.team4213.lib14;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import edu.wpi.first.wpilibj.DriverStation;

public class CowCamController<T>{
	
	
	
	private VideoCapture videoCapture = new VideoCapture();;
    private Mat camImage = new Mat();
    // Future for the Executed Image Processing
    private Optional<Future<T>> processFuture;
    // Output from the Image Process Task
    private T dataOutput;
    // Task Given to Execute
    private Optional<Callable<T>> task;
    // Boolean to Handle Running State
    private volatile boolean isRunning = false;
    
    private ReadWriteLock imageLock = new ReentrantReadWriteLock();
    private ReadWriteLock dataOutputLock = new ReentrantReadWriteLock();

    
	
	public CowCamController(int cameraPort, int fps, Optional<Callable<T>> imgProcess){
		
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
		
		// Set the Camera's Settings
		setCameraOptions();
		
		task = imgProcess;
		
		// Reads first Image
		videoCapture.read(camImage);
				
	}
	
	
	/*
	 * Sets the Camera's Settings
	 */
	private void setCameraOptions(){
    	videoCapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH ,320);
    	videoCapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT , 240);
	}

	
	// Converts a Mat to a Byte Array
	public byte[] getImgAsBytes(){
		
		// Sets JPEG Quality to 10*50
        MatOfInt params = new MatOfInt(Highgui.IMWRITE_JPEG_QUALITY, 10*50);

        // Creates a Mat of Bytes
		MatOfByte matByte = new MatOfByte();
		
		// Encoded the Image into JPEG and Stores it in the Mat of Bytes
		imageLock.readLock().lock();
		Highgui.imencode(".jpg", camImage, matByte, params);
		imageLock.readLock().unlock();

		// Returns the Mat of Bytes as an Array
		return matByte.toArray();
	}
	
	public Mat getImg(){
		imageLock.readLock().lock();
		Mat img = camImage.clone();
		imageLock.readLock().unlock();
		return img;
	}
	
	public T getDataOutput(){
		dataOutputLock.readLock().lock();
		T output = dataOutput;
		dataOutputLock.readLock().unlock();
		
		return output;
		
	}
	
	/*
	 * Public function to Start the Infinite Camera Loop + Processing Task
	 */
	public void start(ExecutorService executor) {
		isRunning = true;
		// Then We run the Infinite Camera Read Loop
		executor.submit(readCamera(executor));
		
	}
	public void stop(){
		isRunning = false;
	}
	/*
	 * Private for creating the Infinite Camera Loop ( Blocking ) 
	 */
	private Runnable readCamera(ExecutorService executor){
		// Returns a Runnable Instance and Returns it
		return ()->{
			// Runs Task for the First Time
			processFuture = task.isPresent() ? Optional.of(executor.submit(task.get())) : Optional.empty();

			while(true){
				// Reads Camera to an Image Variable
				imageLock.writeLock().lock();
				videoCapture.read(camImage);
				imageLock.writeLock().unlock();
				
				
				// Submits Task to Run Async + Get its future if The Process Finished.
				if(task.isPresent() && processFuture.isPresent() && processFuture.get().isDone()){
						// Gets Data from the Future
						dataOutputLock.writeLock().lock();
						dataOutput = processFuture.get().get();
						dataOutputLock.writeLock().unlock();
						// Replaces the Old Future with a New One
						processFuture = Optional.of(executor.submit(task.get()));
				}
			}
		};
	}
	
	
}
