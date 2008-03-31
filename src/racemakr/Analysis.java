package racemakr;

import processing.core.*;
import racemakr.structure.RaceContainr;

public class Analysis {
	private ProcessingSketch pSketch;
	private PImage mugshot;
	private RaceContainr _racistProfile;

	public Analysis(ProcessingSketch p) {
		this.pSketch = p;
	}

	public void init(RaceContainr r) {
		mugshot = new PImage();
		mugshot = pSketch.loadImage("../data/cropped.jpg");
		_racistProfile = r;
	}

	public void drawResult() {
		pSketch.fill(50);
		pSketch.textAlign(PApplet.CENTER, PApplet.TOP);
		pSketch.textFont(pSketch.getTitleFont(), 72);
		pSketch.textLeading(60);

		String title1_str = "this individual is";
		String title2_str = _racistProfile.get_label() + ".";

		pSketch.text(title1_str, pSketch.width >> 1, 50);
		pSketch.text(title2_str, pSketch.width >> 1, 110);

		int gutter = 80;
		int mugY = 160 + gutter;

		pSketch.image(mugshot, (pSketch.width - mugshot.width) / 2, mugY);

		pSketch.noFill();
		pSketch.strokeWeight(10);
		pSketch.stroke(0, 255);
		pSketch.ellipse(pSketch.width >> 1, mugY + (mugshot.height >> 1),
				mugshot.width, mugshot.width);

		pSketch.fill(100);
		pSketch.textAlign(PApplet.CENTER, PApplet.TOP);
		pSketch.textFont(pSketch.getSentenceFont(), 18);

		String[] sentences = _racistProfile.get_sentences();
		float y = mugY + mugshot.height + gutter;

		pSketch.textAlign(PApplet.CENTER);

		// pick out the first sentence for now
		pSketch.text("\"" + sentences[0] + "\"", pSketch.width * .25f, y,
				pSketch.width * .5f, 500f);
		
		// for (int i = 0; i < sentences.length; i++) {
		// pSketch.text(sentences[i], pSketch.width * .25f, y,
		// pSketch.width * .5f, 500f);
		// y += 100f;
		// }
	}
}
