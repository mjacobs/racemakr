package racemakr;

import java.io.File;
import processing.core.*;
import FaceDetect.*;

/**
 * Face detection
 * 
 * Starts webcam driver and provides calls for face tracking + image
 * 
 * 
 * work in progress!
 * 
 * In a nutshell:
 * Call getPImage() to retrieve a PImage of webcam (whether a face was detected or not),
 * or getImage() for an int[] of the same thing.
 * 
 * Call getFaceData() to get a 2-dim array: a x,y,r triple for each face detected (null if no faces)
 * (see function for more details)
 * 
 * 
 * @author Chuan
 * 
 */

public class FaceCapture {
	private ProcessingSketch p;
	private static PImage webcam;
	private static FaceDetect fd;
	private static File fcap;
	
	private int webcamX, webcamY;
	
	private int MAX = 1;			// maximum number of faces detectable per image, setting this to 1 for now
	private int[][] Faces = new int[MAX][3];

	public FaceCapture(ProcessingSketch p, int w, int h, int r) {
		this.p = p;
		
		webcam = p.createImage(w,h,PApplet.RGB);

		// find center of stage
		webcamX = (p.width-w)/2;
		webcamY = (p.height-h)/2;
		
		fd = new FaceDetect();
		fd.start(w, h, r);
		System.out.println(fd.version());
	}
	
	public int[][] getFaceData() {
		/**
		 * returns 2-deep array containing a triple for each face detected in the image
		 * triple refers to [x], [y], and [radius] of detected face, or null if no face is found
		 */
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
	
	public PImage getPImage() {
		webcam.loadPixels();
		PApplet.arraycopy(getImage(), webcam.pixels);
		webcam.updatePixels();
		
		return(webcam);
	}
	
	public void drawImage() {
		int[][] faceData = getFaceData();

		p.image(getPImage(), webcamX, webcamY);		
		p.strokeWeight(10);
		p.stroke(255);
		p.noFill();
		
		// TODO smooth out movement of tracking ellipse for that sleek effect
		if(faceData!=null) {
			for (int i = 0; i < faceData.length; i++) {
				p.ellipse(webcamX+faceData[i][0], webcamY+faceData[i][1], faceData[i][2]*2, faceData[i][2]*2);
			}
		} else {
			p.timer.reset();
		}
	}

	public void drawCapture() {
		p.image(webcam, webcamX, webcamY);
		p.strokeWeight(10);
		p.stroke(200,0,0);
		p.noFill();
		
		for (int i = 0; i < Faces.length; i++) {
			p.ellipse(webcamX+Faces[i][0], webcamY+Faces[i][1], Faces[i][2]*2, Faces[i][2]*2);
		}
	}
	
	
	public void doGrab() {
		// TODO Matt, this function will be eventually linked to your magic stuff
		String filename = saveImage();		// filename of saved png
		PImage grabImage = getPImage();		// PImage
		int[][] faceData = getFaceData();	// int[][] of face coordinates		
	}
	
	public String saveImage() {
		/**
		 * This function currently saves out an raw PNG file to the bin folder as captureX.png
		 * (every time the sketch is restarted it starts at 1 again; this should be ok for now
		 * but eventually should increment perpetually without overwriting older images)
		 */
				
		String filename = "capture"+getLastImgCount()+".png";
		System.out.println("Saving image: " + filename);

		webcam.save(filename);
		
		return filename;
	}
	
	private int getLastImgCount() {
		int c = 1;
		
		// finds the next filename to continue from
		fcap = new File("capture1.png");
		
		while(fcap.exists()) {
			c++;
			fcap = new File("capture"+c+".png");
		}
		
		System.out.println("new file:" + c);
		return c;
	}
	
	public void stop() {
		fd.stop();
	}

}
