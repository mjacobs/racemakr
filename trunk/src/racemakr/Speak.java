package racemakr;

import rita.*;



public class Speak {
	RiSpeech rs;
	
	public Speak() {
		RiSpeech.setTTSEnabled(true);
		rs = new RiSpeech(null);
		rs.setVoiceRate(120);
	}
	
	void say(RiText text){
		rs.speak(text);
	}


}

