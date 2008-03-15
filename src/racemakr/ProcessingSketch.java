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

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "--bgcolor=#111111",
				"ProcessingSketch" });
	}

	public void setup() {
		size(640, 480);
		fc = new FaceCapture(this, width, height, 65);
	}

	public void draw() {
		background(0);
		fc.drawImage();				
	}

	public void keyPressed() {
		if (keyCode == 32) {
			println("begin capturing face...");

		}
	}

	public void stop() {
		super.stop();
	}

}
