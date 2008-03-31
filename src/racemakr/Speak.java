package racemakr;

import rita.*;

public class Speak {
	ProcessingSketch pSketch;
	RiSpeech rs;

	public Speak(ProcessingSketch p) {
		this.pSketch = p;

		RiSpeech.setTTSEnabled(true);
		rs = new RiSpeech(null);
		rs.setVoiceRate(120);
	}

	public void say(String text) {
		rs.speak(text);
	}

}
