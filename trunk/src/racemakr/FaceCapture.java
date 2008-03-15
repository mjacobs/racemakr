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
	private ProcessingSketch p;
	private PImage webcam;
	private static FaceDetect fd;
	
	int MAX = 1;			// maximum number of faces, setting this to 1 for now

	int[] x = new int[MAX];
	int[] y = new int[MAX];
	int[] r = new int[MAX];
	int[][] Faces = new int[MAX][3];

	public FaceCapture(ProcessingSketch p, int w, int h, int r) {
		this.p = p;
		
		fd = new FaceDetect();
		fd.start(w, h, r);
		System.out.println(fd.version());		
	}
	
	public int[][] getFace() {
		// TODO return int triple

		Faces = fd.detect();
		int count = Faces.length;
		if (count > 0) {
			return(Faces);		
		}
		return null;
	}
	
	
	public int[] getImage() {
		return fd.image();
	}
	
	
	/*
	public void drawFace() {
		int[] img = fd.image();
		p.loadPixels();
		ArrayCopy(img, p.pixels);
		p.updatePixels();
		
		p.strokeWeight(2);
		p.stroke(255, 200, 0);
		p.noFill();
		for (int i = 0; i < count; i++) {
			PApplet.ellipse(x[i], y[i], r[i], r[i]);
		}

	}
	*/
	
	public void captureFace(int x, int y, int r) {		
		// TODO forget about this and send a whole image + filename instead
		int block = p.color(255, 0, 0);		
		int startx = x-r;
		int starty = y-r;
		int endx = x+r;
		int endy = y+r;
		
		p.loadPixels();
		for (int i = ((starty-1)*p.width)-startx; i < (endy*p.width)+endx; i++) {
			p.pixels[i] = block;
		}
		p.updatePixels();
	}

	
	public void stopFaceDetect() {
		fd.stop();
	}

	public void drawImage() {
		int[][] faceData = getFace();
		p.loadPixels();
		PApplet.arraycopy(getImage(), p.pixels);
		p.updatePixels();

		p.strokeWeight(2);
		p.stroke(255, 200, 0);
		p.noFill();
		
		if(faceData!=null) {
			for (int i = 0; i < faceData.length; i++) {
				p.ellipse(faceData[i][0], faceData[i][1], faceData[i][2]*2, faceData[i][2]*2);
			}
		}
		
	}
}
