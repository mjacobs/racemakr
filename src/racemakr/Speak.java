package racemakr;

import rita.*;

public class Speak {
	ProcessingSketch pSketch;
	RiSpeech rs;

	public Speak(ProcessingSketch p) {
		this.pSketch = p;

		RiSpeech.setTTSEnabled(true);
		rs = new RiSpeech(null);
		rs.setVoicePitch(60);
		rs.setVoiceRate(120);

		say("Race Maker is now active");
	}

	public void say(String text) {
		rs.speak(text);
	}
}
