package racemakr;

import processing.core.*;

/**
 * 
 * This will be the processing class that will launch the rest of the classes
 * 
 * 
 */

public class ProcessingSketch extends PApplet {
	FaceCapture fc;
	static int[] capturedim = {320, 240};
	Timer timer;
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "--bgcolor=#111111",
				"ProcessingSketch" });
	}

	public void setup() {
		size(1024, 768);		
		// face detection radius should optimally be about 1/4726 of total number of pixels
		float radratio = (capturedim[0]*capturedim[1])/4726; 
		fc = new FaceCapture(this, 640, 480, (int)radratio);
		timer = new Timer(this);
		smooth();
	}

	// MAIN DRAW LOOP
	public void draw() {
		background(0);
		
		timer.update();
		fc.drawImage();
	}

	public void keyPressed() {
		// for debugging, wil be removed eventually...
		if (keyCode == 32) {
			println("begin capturing face...");
		}
	}

	public void stop() {
		fc.stop();
		super.stop();
	}
	
}
