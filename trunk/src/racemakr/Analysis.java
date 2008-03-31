package racemakr;

import processing.core.*;
import racemakr.structure.RaceContainr;

public class Analysis {
	private PApplet pSketch;
	private PImage mugshot;
	private RaceContainr _racistProfile;
	private PFont titleFont;
	
	public Analysis(ProcessingSketch p) {
		this.pSketch = p;

		titleFont = pSketch.loadFont("../data/TradeGothicLTStd-Bd2-36.vlw");
	}

	public void init(RaceContainr r) {
		mugshot = new PImage();
		mugshot = pSketch.loadImage("../data/cropped.jpg");
		_racistProfile = r;
	}

	public void drawResult() {
		pSketch.fill(50);
		pSketch.textAlign(PApplet.CENTER, PApplet.TOP);
		pSketch.textFont(titleFont, 36);
		
		pSketch.text("This individual is determined to be " + _racistProfile.get_label(), pSketch.width >> 1, 50);
		pSketch.image(mugshot, (pSketch.width-mugshot.width)/2, (pSketch.height-mugshot.height)/2);
	}
}
