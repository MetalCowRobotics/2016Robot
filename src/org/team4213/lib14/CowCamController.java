package org.team4213.lib14;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

/**
 * <p>
 * CowCamController is a very simple class that handles most of what one would
 * do with a Camera. It reads the images and then runs a task upon them. The
 * implementation is simple consisting of a few methods that one must deal with
 * and is ready to handle multiple types of data using generics. It also uses
 * <code>Optional</code>s to keep <code>null</code> at bay.
 * <code>Optional</code>s also make it easy to not worry about the Task if one
 * simply wanted camera reading functionality.
 * </p>
 * <p>
 * The class manages
 * <ul>
 * <li>it's own running state</li>
 * <li>a <code>VideoCapture</code> instance</li>
 * <li>an <code>Optional< Callable < T > ></code>(the Task></li>
 * <li>an <code>Optional< Future < T > ></code> to wait for the output of the
 * Task</li>
 * <li>an <code>Optional < T > </code> to the actual output of the Task</li>
 * <li>a <code>Mat</code> for it's camera's latest image</li>
 * <li>and 2 <code>ReadWriteLock<code>s to prevent from issues with multiple
 * threads</li>
 * </ul>
 * </p>
 * 
 * @author KrithikR
 */

public class CowCamController<T> {

	// The Video Stream from which we Get the Images
	private VideoCapture videoCapture = new VideoCapture();;
	// The Image Being Streamed from the Camera
	private Mat camImage = new Mat();
	// Future for the Executed Image Processing
	private Optional<Future<T>> processFuture;
	// Output from the Image Process Task
	private Optional<T> dataOutput;
	// Task Given to Execute
	private Optional<Callable<T>> task;
	// Boolean to Handle Running State
	private volatile boolean isRunning = false;
	// The Locks for the Two Cross Thread Accessed Variables
	private ReadWriteLock imageLock = new ReentrantReadWriteLock();
	private ReadWriteLock dataOutputLock = new ReentrantReadWriteLock();

	/**
	 * Initializes a Camera Controller with a Port , Camera FPS , And an
	 * Optional Task to Run. The port and fps are <code>int</code>(s) whereas
	 * the Optional Task is a <code>Callable<T></code>
	 * 
	 * @param cameraPort
	 *            the integer to specify upon which port the Camera is
	 * @param fps
	 *            the integer to specify the frames per second of the Camera
	 *            Read
	 * @param imgProcess
	 *            <code>Optional<Callable<T>><code> that runs on each Camera Read, 
	 *            which the output of can be retrieved by the <code>getDataOutput</code>
	 *            method
	 * 
	 * @see DriverStation
	 * @see VideoCapture
	 * @see Optional
	 * @see Callable
	 */
	public CowCamController(int cameraPort, int fps, Optional<Callable<T>> imgProcess) {

		DriverStation.reportError("fancy", false);

		// Attempts to Open the Camera
		while (true) {
			try {
				videoCapture.open(cameraPort, 320, 240, fps);
			} catch (Exception ex) {
				continue;
			}
			break;
		}

		// Set the Camera's Settings
		setCameraOptions();

		// Sets the Task to The Task being passed In
		task = imgProcess;

		// Reads first Image
		videoCapture.read(camImage);

	}

	/**
	 * Sets the Camera's Settings when being read. Current implementation sets
	 * the resolution to 320*240px;
	 * 
	 * @see VideoCapture
	 */
	private void setCameraOptions() {
		videoCapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 320);
		videoCapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 240);
	}

	/**
	 * Returns a Byte Array from the Camera's Image. This method is safe for
	 * usage by multiple threads.
	 * <p>
	 * <strong>Summary<strong><br>
	 * Current implementation first sets the JPEG Quality to 500 in a
	 * <code>MatOfInt</code> called params ( short for parameters ). Then
	 * creates a <code>MatOfByte</code> called matByte to store the Image
	 * within. A lock is then set in place for the read and encode operation of
	 * the Camera's image. This operation also takes a filetype as a
	 * <code>String</code> ".jpeg" and the previously created parameters
	 * <code>MatOfInt</code> and matByte <code>MatOfByte</code>. The encode
	 * function fills the <code>MatOfByte</code> with the Image in the
	 * appropriate format according to the params and file extention. The lock
	 * on the image is then unlocked before returning the filled
	 * <code>MatOfByte</code> as a <code>Byte[]</code>
	 * </p>
	 * 
	 * @return <code>byte[]</code> created from the Camera's Image
	 * 
	 * @see MatOfInt
	 * @see MatOfByte
	 * @see Highgui
	 * @see ReadWriteLock
	 */
	public byte[] getImgAsBytes() {

		// Sets JPEG Quality to 10*50
		MatOfInt params = new MatOfInt(Highgui.IMWRITE_JPEG_QUALITY, 10 * 50);

		// Creates a Mat of Bytes
		MatOfByte matByte = new MatOfByte();

		// Encoded the Image into JPEG and Stores it in the Mat of Bytes
		imageLock.readLock().lock();
		Highgui.imencode(".jpg", camImage, matByte, params);
		imageLock.readLock().unlock();

		// Returns the Mat of Bytes as an Array
		return matByte.toArray();
	}

	/**
	 * Returns a Clone of the Camera's Image. It's implementation is meant to be
	 * thread safe.
	 * <p>
	 * <strong>Summary</strong><br>
	 * The <code>ReadWriteLock</code> {@link #imageLock} is first locked for
	 * reading before the latest image from the camera , {@link #camImage},
	 * which is a <code>Mat</code> is cloned and put into the newly created
	 * <code>Mat</code> called img. The lock is then unlocked and the cloned
	 * image (<code>Mat</code>) is returned.
	 * </p>
	 * 
	 * @return <code>Mat</code> that is a clone of the latest camera image
	 * 
	 * @see Mat
	 * @see ReadWriteLock
	 */
	public Mat getImg() {

		imageLock.readLock().lock();
		Mat img = camImage.clone();
		imageLock.readLock().unlock();

		return img;
	}

	/**
	 * Returns the Output of the Image Processing Task. It's implementation is
	 * meant to be thread safe.
	 * 
	 * <p>
	 * <strong>Summary</strong> <br>
	 * The <code>ReadWriteLock</code> {@link #dataOutputLock} is first locked
	 * for reading. A new variable <code>output</code> of type <code>T</code> is
	 * then assigned to the existing variable called <code>dataOutput</code>
	 * that contains the output of the image processing task. Then the
	 * {@link #dataOutputLock} is unlocked for reading and the
	 * <code>T<code> stored in <code>output</code> is returned.
	 * </p>
	 * 
	 * @return Object of type <code>T<code> that contains periodic output of the
	 *         image processing task
	 * 
	 * @see ReadWriteLock
	 */
	public Optional<T> getDataOutput() {

		dataOutputLock.readLock().lock();
		Optional<T> output = dataOutput;
		dataOutputLock.readLock().unlock();

		return output;

	}

	/**
	 * Returns the Current State of the Camera Controller. It is thread safe for
	 * reading and returns {@link #isRunning}.
	 * 
	 * @return <code>boolean</code> that represents the current state of the
	 *         camera controller ( whether the camera is being read or not )
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Adds a New Camera Read and Process Loop to the Inputted Executor and sets
	 * Running to True. The method handles the creation of a infinite loop that
	 * is run on a seperate thread pool created by an Executor.
	 * 
	 * @param executor
	 *            an <code>ExecutorService</code> that can be used to run the
	 *            Camera Loop
	 * 
	 * @see ExecutorService
	 * @see #readCamera(ExecutorService)
	 */
	public void start(ExecutorService executor) {
		// Stops old Execution
		stop();
		// Sets Current Running State to True
		isRunning = true;
		// Then We run the Infinite Camera Read Loop
		executor.submit(readCamera(executor));
	}

	/**
	 * Stops the infinite camera read and process loop running in the
	 * background.
	 * 
	 * @see #readCamera(ExecutorService)
	 */
	public void stop() {
		isRunning = false;
	}

	/**
	 * Returns the Runnable for the Infinite Camera Reading Loop. The returned
	 * Runnable handles many cases of <code>Optional</code>s not existing and is
	 * properly made thread safe. The method is private so it's only called upon
	 * at a reasonable time by the {@link #start(ExecutorService)} function.
	 * 
	 * @param executor
	 *            an <code>ExecutorService</code> that can be used to run the
	 *            Image Processing Task
	 * 
	 * @see #start(ExecutorService)
	 * @see Future
	 * @see Callable
	 * @see VideoCapture
	 * @see Optional
	 * @see ReadWriteLock
	 * @see Runnable
	 */
	private Runnable readCamera(ExecutorService executor) {
		// Returns a Runnable Instance and Returns it
		return () -> {
			// Runs Task for the First Time
			processFuture = task.isPresent() ? Optional.of(executor.submit(task.get())) : Optional.empty();

			while (true) {
				// Reads Camera to an Image Variable
				imageLock.writeLock().lock();
				videoCapture.read(camImage);
				imageLock.writeLock().unlock();

				// Submits Task to Run Async + Get its future if The Process
				// Finished.
				if (task.isPresent() && processFuture.isPresent() && processFuture.get().isDone()) {
					// Updates Data from the Future
					dataOutputLock.writeLock().lock();
					try {
						dataOutput = Optional.of(processFuture.get().get());
					} catch (InterruptedException e) {
						DriverStation.reportError(e.getMessage(), true);
					} catch (ExecutionException e) {
						DriverStation.reportError(e.getMessage(), true);
					}
					dataOutputLock.writeLock().unlock();
					// Replaces the Old Future with a New One
					processFuture = Optional.of(executor.submit(task.get()));
				}
			}
		};
	}

}
