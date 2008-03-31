package racemakr;

import java.io.File;

import processing.core.*;
import racemakr.profilr.Profilr.Race;

/**
 * History renderer
 * 
 * Loads all previously capture images and displays in a grid
 * 
 * work in progress!
 * 
 * 
 * @author Chuan
 * 
 */

public class History {
	private ProcessingSketch pSketch;
	private PImage[] historyList;

	private static final int gridSize = 64;
	private static final int w = 128;
	private static final int h = 96;
	private int currentImg;
	private int maxCols, maxRows;

	public History(ProcessingSketch p) {
		this.pSketch = p;
		historyList = new PImage[gridSize];

		// for (int i = 0; i < gridSize; i++) {
		// historyMap.put(new Integer(i),new PImage());
		// }

		maxCols = pSketch.width / w;
		maxRows = pSketch.height / h;

		init();
	}

	public void init() {
		File fcap;

		// look for all recently captured files
		for (int i = 0; i < gridSize; i++) {

			// finds the next filename to continue from
			fcap = new File("../data/history/capture" + (i + 1) + ".jpg");

			if (fcap.exists()) {
				historyList[i] = pSketch.loadImage(fcap.toString());
				System.out.println(historyList[i]);

				currentImg = i;
			}
		}
		System.out.println("captured images loaded OK");
	}

	public void addNew() {
		currentImg++;
		if (currentImg > gridSize-1)
			currentImg = 0;

		// finds the next filename to continue from
		File fcap = new File("../data/history/capture" + (currentImg + 1)
				+ ".jpg");
		if (fcap.exists()) {
			historyList[currentImg] = pSketch.loadImage(fcap.toString());
		}
	}

	public void draw() {
		int xcoord, ycoord;

		ycoord = -1;

		for (int i = 0; i < gridSize; i++) {
			xcoord = i % maxCols;
			if (xcoord == 0) {
				ycoord++;
			}

			if (historyList[i] != null) {
				pSketch.image((PImage) historyList[i], xcoord * w, ycoord * h,
						w, h);
			}
		}
	}
	
}
