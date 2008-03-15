package racemakr;

import processing.core.*;
import FaceDetect.*;

/**
 * Face detection
 * 
 * Invokes webcam driver and starts face tracking
 * 
 * 
 * work in progress
 * 
 * @author Chuan
 * 
 */

public class FaceCapture {
	PApplet p;
	FaceCapture fd;
	int MAX = 1;			// maximum number of faces, setting this to 1 for now

	int[] x = new int[MAX];
	int[] y = new int[MAX];
	int[] r = new int[MAX];
	int[][] Faces = new int[MAX][3];

	public FaceCapture(PApplet p) {
		this.p = p;
		init();
	}
	
	private void init() {
		fd = new FaceDetect();
		fd.start(width, height, 50);
		System.out.println(fd.version());		
	}

	public void getDetect() {
		// TODO return int triple

		Faces = fd.detect();
		int count = Faces.length;
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				x[i] = Faces[i][0];
				y[i] = Faces[i][1];
				r[i] = Faces[i][2] * 2;
				// println(i + 1 + ": " + x[i] + ", " + y[i] + ", " + r[i]);
				// captureFace(x[i], y[i], r[i]);
			}
		}
	}
	
	public void drawFace() {
		int[] img = fd.image();
		loadPixels();
		arraycopy(img, pixels);
		updatePixels();
		
		strokeWeight(2);
		stroke(255, 200, 0);
		noFill();
		for (int i = 0; i < count; i++) {
			ellipse(x[i], y[i], r[i], r[i]);
		}

	}
	
	public void captureFace(int x, int y, int r) {		
		// TODO forget about this and send a whole image + filename instead
		int block = color(255, 0, 0);		
		int startx = x-r;
		int starty = y-r;
		int endx = x+r;
		int endy = y+r;
		
		PApplet.loadPixels();
		for (int i = ((starty-1)*width)-startx; i < (endy*width)+endx; i++) {
			PApplet.pixels[i] = block;
		}
		PApplet.updatePixels();
	}

	
	public void stopFaceDetect() {
		fd.stop();
	}
	
	
}
