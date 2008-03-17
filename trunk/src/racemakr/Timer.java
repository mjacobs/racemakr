package racemakr;

public class Timer {
	private ProcessingSketch p;
	private int ms;
	
	private static int timeout = 3000;
	public enum Mode { INIT, DETECT, PREANALYZE, ANALYZE, HISTORY };
	private static Mode currentMode;
	
	public Timer(ProcessingSketch p) {
		this.p = p;
		
		// INIT reserved for intro presentation in 2nd iteration
		setMode(Mode.INIT);
		reset();
	}
	
	
	public void update() {
		int delta = p.millis()-ms;
		if(delta>timeout) {
			
			switch(currentMode) {
				case INIT:
					// done with the intro splash screen, now instantiate webcam and start face detection
					break;
					
					
				case DETECT:
					System.out.println("CAPTURE!");					
					setTimeout(2500);
					setMode(Mode.PREANALYZE);
					break;

				case PREANALYZE:
					System.out.println("done waiting!");
					setTimeout(5000);
					setMode(Mode.ANALYZE);
					break;

				case ANALYZE:
					System.out.println("analysis display complete");
					setTimeout(5000);
					setMode(Mode.HISTORY);					
					break;
					
				case HISTORY:
					System.out.println("history display complete");
					setTimeout(5000);
					setMode(Mode.DETECT);					
					
					break;
			}			
			reset();
		}
		
	}
	
	public void reset() {
		ms = p.millis();
	}
	
	public void setMode(Mode newMode) {
		currentMode = newMode;
	}
	
	public Mode getMode() {
		return currentMode;
	}
	
	public void setTimeout(int t) {
		timeout = t;
	}
}
