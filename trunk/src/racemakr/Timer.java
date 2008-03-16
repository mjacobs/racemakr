package racemakr;

import processing.core.*;

public class Timer {
	private ProcessingSketch p;
	private int ms;
	
	private static int captureTimeout = 3000;
	public enum Mode { INIT, DETECT, ANALYZE, HISTORY };
	private static Mode currentMode;
	
	public Timer(ProcessingSketch p) {
		this.p = p;
		
		// INIT reserved for intro presentation in 2nd iteration
		setMode(Mode.DETECT);
		reset();
	}
	
	
	public void update() {
		int delta = p.millis()-ms;
		if(delta>captureTimeout) {
			
			if(currentMode==Mode.DETECT) {
				System.out.println("do capture!");
				p.fc.doGrab();				
			}
						
			reset();
		}
		
	}
	
	public void reset() {
		ms = p.millis();
	}
	
	public static void setMode(Mode newMode) {
		currentMode = newMode;
	}
	
	public static Mode getMode() {
		return currentMode;
	}
}
