package racemakr;

import processing.core.PApplet;
import processing.core.PImage;

public class Analysis {
	PApplet pSketch;
	PImage mugshot;
	
	public Analysis(ProcessingSketch p) {
		this.pSketch = p;
		
	}
	
	public void refresh() {
		mugshot = new PImage();
		mugshot = pSketch.loadImage("../data/cropped.jpg");
	}
	
	public void drawResult() {
		
		pSketch.image(mugshot, 200, 200);
		
	}
}
