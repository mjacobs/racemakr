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
	
	public History(ProcessingSketch p) {
		this.p = p;
		historyList = new PImage[gridSize];
		
		init();
	}
	
	public void init() {
		File fcap;
		PImage test;
		
		for(int i=0;i<gridSize+1;i++) {
			
			// finds the next filename to continue from
			fcap = new File("capture"+i+".png");			
			
			if(fcap.exists()) {
				//System.out.println(fcap.toString());
				//historyList[i] = p.loadImage(fcap.toString());
				test = p.loadImage(fcap.toString());
				
			}
		}
		
		//System.out.println(historyList);
	}
}

