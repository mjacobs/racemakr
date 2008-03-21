package racemakr;

import java.io.File;

import processing.core.*;

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
	private ProcessingSketch p;
	private PImage[] historyList;
	private static final int gridSize = 64;
	private static final int w = 128;
	private static final int h = 96;
	private int maxCols, maxRows;
	
	public History(ProcessingSketch p) {
		this.p = p;
		historyList = new PImage[gridSize];
		
		maxCols = p.width/w;
		maxRows = p.height/h;

		init();
	}
	
	public void init() {
		File fcap;
		
		// look for all recently captured files
		for(int i=0;i<gridSize;i++) {
			
			// finds the next filename to continue from
			fcap = new File("capture"+(i+1)+".jpg");			
			
			if(fcap.exists()) {
				System.out.println(fcap.toString());
				historyList[i] = p.loadImage(fcap.toString());
				System.out.println(historyList[i]);
				
			}
		}
		
		System.out.println(historyList[0]);			
	}
	
	public void draw() {
		int xcoord, ycoord;
		
		ycoord = -1;
		
		for(int i=0;i<gridSize;i++) {
			xcoord = i%maxCols;
			if(xcoord==0) {
				ycoord++;
			}
			
			if(historyList[i]!=null)
				p.image(historyList[i], xcoord*w, ycoord*h, w, h);
		}
	}
	
}

