package racemakr;

public class Timer {
	private ProcessingSketch pSketch;
	private int ms;
	private static int timeout = 3000;

	public enum Mode {
		INIT, DETECT, PREANALYZE, ANALYZE, HISTORY
	};

	private static Mode currentMode;

	private boolean _speakonce;

	public Timer(ProcessingSketch p) {
		this.pSketch = p;

		setMode(Mode.INIT);
		setTimeout(12000);
		reset();
	}

	public void update() {
		int delta = pSketch.millis() - ms;
		
		if (currentMode == Mode.DETECT) {
			pSketch.fc.drawCountdown(3000 - delta);
		}

		if (delta > timeout) {
			switch (currentMode) {
			case INIT:	
				// done with the intro splash screen, now instantiate webcam and
				// start face detection
				setMode(Mode.DETECT);
				break;

			case DETECT:
				System.out.println(">>> CAPTURE!");
				setTimeout(3000);
				setMode(Mode.PREANALYZE);
				break;

			case PREANALYZE:
				System.out.println(">>> DONE WAITING AFTER CAPTURE");
				setTimeout(5000);
				setMode(Mode.ANALYZE);
				_speakonce = true;
				pSketch.fc.doAnalyze();
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
		ms = pSketch.millis();
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
