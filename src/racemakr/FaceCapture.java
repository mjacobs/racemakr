package racemakr;

import java.io.File;
import processing.core.*;
import racemakr.datastructs.RaceContainr;
import racemakr.profilr.Profilr;
import FaceDetect.FaceDetect;

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
 * or getImage() for an int[] of the same thing
 * 
 * Call getFaceData() to get a 2-dim array: a x,y,r triple for each face detected (null if no faces)
 * (see function for more details)
 * 
 * 
 * @author Chuan
 * 
 */

public class FaceCapture {
	private static final int NUM_SENTENCES = 8;
	private ProcessingSketch pSketch;
	private PImage _camSnapshot;
	private FaceDetect _faceDetect;
	private RaceContainr _racistProfile;
	
	private int _camCenterX, _camCenterY;
	
	private int MAX_FACES = 1;			// maximum number of faces detectable per image, setting this to 1 for now
	private int[][] facesArray = new int[MAX_FACES][3];

	public FaceCapture(ProcessingSketch p, int w, int h, int r) {
		pSketch = p;
		_camSnapshot = p.createImage(w,h,PApplet.RGB);

		// find center of stage
		_camCenterX = (p.width-w)/2;
		_camCenterY = (p.height-h)/2;
		
		_faceDetect = new FaceDetect();
		_faceDetect.start(w, h, r);
		System.out.println(_faceDetect.version());
	}
	
	public int[][] getFaceData() {
		/**
		 * returns 2-deep array containing a triple for each face detected in the image
		 * triple refers to [x], [y], and [radius] of detected face, or null if no face is found
		 */
		facesArray = _faceDetect.detect();
		int count = facesArray.length;
		
		if (count > 0) {
			return facesArray;		
		}
		
		return null;
	}
	
	public int[] getImage() {
		return _faceDetect.image();
	}
	
	public PImage getPImage() {
		_camSnapshot.loadPixels();
		PApplet.arraycopy(getImage(), _camSnapshot.pixels);
		_camSnapshot.updatePixels();
		
		return(_camSnapshot);
	}
	
	public void drawImage() {
		// Get the faces in the image
		int[][] faceData = getFaceData();

		// Set the look of the circles
		pSketch.image(getPImage(), _camCenterX, _camCenterY);		
		pSketch.strokeWeight(10);
		pSketch.stroke(255);
		pSketch.noFill();
		
		// Draw a circle around each face in the image
		// TODO smooth out movement of tracking ellipse for that sleek effect
		if(faceData!=null) {
			for (int i = 0; i < faceData.length; i++) {
				pSketch.ellipse(_camCenterX+faceData[i][0], _camCenterY+faceData[i][1], faceData[i][2]*2, faceData[i][2]*2);
			}
		} else {
			pSketch.timer.reset();
		}
	}

	public void drawCapture() {
		pSketch.image(_camSnapshot, _camCenterX, _camCenterY);
		pSketch.strokeWeight(10);
		pSketch.stroke(200,0,0);
		pSketch.noFill();
				
		for (int i = 0; i < facesArray.length; i++) {
			pSketch.ellipse(_camCenterX+facesArray[i][0], _camCenterY+facesArray[i][1], facesArray[i][2]*2, facesArray[i][2]*2);
		}
	}
	
	public void doGrab() {
		// TODO Matt, this function will be eventually linked to your magic stuff
		String filename = saveImage();		// filename of saved png
		PImage grabImage = getPImage();		// PImage
		int[][] faceData = getFaceData();	// int[][] of face coordinates	
		Profilr profilr = new Profilr(pSketch,filename,facesArray[0][0],facesArray[0][1],facesArray[0][2],NUM_SENTENCES);
		_racistProfile = profilr.getProfile();
		_racistProfile.print();
	}
	
	/**
	 * This function currently saves out an raw JPG file to the bin folder as captureX.jpg
	 * (every time the sketch is restarted it starts at 1 again; this should be ok for now
	 * but eventually should increment perpetually without overwriting older images)
	 */
	public String saveImage() {				
		String filename = "capture"+getLastImgCount()+".jpg";
		System.out.println("Saving image: " + filename);

		_camSnapshot.save(filename);
		
		return filename;
	}
	
	private int getLastImgCount() {
		File fcap;
		int c = 1;
		
		// finds the next filename to continue from
		fcap = new File("capture1.jpg");
		
		while(fcap.exists()) {
			c++;
			fcap = new File("capture"+c+".jpg");
		}
		
		System.out.println("new file:" + c);
		return c;
	}
	
	public void stop() {
		_faceDetect.stop();
	}

}
