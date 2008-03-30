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
	History history;
	PImage splashImg;
	PFont headingFont;
	PFont bodyFont;
	PFont countdownFont;
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "--bgcolor=#111111",
				"ProcessingSketch" });
	}

	public void setup() {
		size(1024, 768);
		frameRate(30);
		
		splashImg = loadImage("../data/splash.png");
		headingFont = loadFont("../data/TradeGothicLTStd-Bold-56.vlw");		
		bodyFont = loadFont("../data/TradeGothicLTStd-20.vlw");
		countdownFont = loadFont("../data/TradeGothicLTStd-Bd2-96.vlw");
		
		// face detection radius should optimally be about 1/4726 of total number of pixels
		float radratio = (capturedim[0]*capturedim[1])/4726; 
		fc = new FaceCapture(this, 640, 480, (int)radratio);
		timer = new Timer(this);
		history = new History(this);	
		smooth();		
	}

	// MAIN DRAW LOOP
	public void draw() {
		background(0);

		switch(timer.getMode()) {
			case INIT:
				drawSplash();
				history.draw();
				//drawHistory();
				break;
			case DETECT:		
				fc.drawDetect();
				break;
			case PREANALYZE:
				// freeze frame and draw captured image
				fc.drawCapture();
				break;
			case ANALYZE:
				drawAnalysis();
				break;
			case HISTORY:
				drawHistory();
				break;
				
		}
		
		timer.update();
	}

	public void keyPressed() {
		switch(timer.getMode()) {
			case INIT:
				timer.setMode(Timer.Mode.DETECT);
				break;
			case DETECT:
				if (keyCode == 32) {
					println("begin capturing face...");
				}
				break;
		}
	}

	public void drawSplash() {
		image(splashImg, 0, 180);			
		fill(200);
		textFont(bodyFont, 16);
		textAlign(CENTER);
		text("press any key to begin.", width>>1, height-10);
	}
	
	public void drawAnalysis() {
		textAlign(CENTER, TOP);
		fill(200);
		textFont(headingFont, 36);
		text("ANALYSIS", width>>1, 20);
	}
	
	public void drawHistory() {
		textAlign(CENTER, TOP);
		fill(200);
		textFont(headingFont, 36);
		text("HISTORY", width>>1, 20);
		
		history.draw();
	}
	
	public void stop() {
   		fc.stop();
		super.stop();
	}
	
}
