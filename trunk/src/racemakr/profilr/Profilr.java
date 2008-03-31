package racemakr.profilr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import racemakr.structure.RaceContainr;
import rita.RiMarkov;

public class Profilr {

	private RiMarkov _rudeGen, _statGen;
	private RaceContainr _profile;
	private PApplet _parent;
	private String _thumbPath;
	private Sandbox _sandbox;

	public enum Race {
		CAUCASIAN, ASIAN, SOUTHAMERICAN, BLACK
	};
	
	private static HashMap<Race, Color> _averageColors;
	private static final int NUM_STRINGS = 10;
	private static final float CAUCASIAN_SCALING = .4f;

	public Profilr(PApplet parent, String filename, String tpath, int x, int y, int rad,
			int num_sentences) {
		_parent = parent;
		
		_sandbox = new Sandbox();
		_sandbox.init();

		// Setup our 'profile' to compare against:
		HashMap<Race, Color> averageColors = new HashMap<Race, Color>();
		averageColors.put(Race.ASIAN, new Color(101, 103, 95));
		averageColors.put(Race.BLACK, new Color(98, 110, 128));
		averageColors.put(Race.CAUCASIAN, new Color(106, 110, 71));
		averageColors.put(Race.SOUTHAMERICAN, new Color(101, 106, 99));
		set_averageColors(averageColors);

		// Figure out what the race is:
		Race r = getRace(filename, rad, x, y);

		_rudeGen = new RiMarkov(parent, num_sentences);
		_rudeGen.loadFile("../text/" + r.name() + "_RUDE.txt");

		_statGen = new RiMarkov(parent, num_sentences);
		_statGen.loadFile("../text/" + r.name() + "_STATISTICS.txt");

		PImage snapshot = parent.loadImage(filename);
		_thumbPath = tpath;
		generateThumb(snapshot, r.toString());
		
		_profile = getProfile(snapshot, r, x, y, rad,
				NUM_STRINGS);
	}
	
	private void generateThumb(PImage snapshot, String label)
	{
		_sandbox.image(snapshot, 0, 0, 160, 120);
		_sandbox.fill(255,200);
		_sandbox.textFont(_sandbox.headingFont);
		_sandbox.textAlign(_sandbox.CENTER);
		_sandbox.text(label, 80, 65);

		PImage thumb = _sandbox.createImage(160, 120, PApplet.RGB);
		thumb.copy(_sandbox.get(), 0, 0, 160, 120, 0, 0, 160, 120);
		thumb.save(_thumbPath);
	}

	public RaceContainr getProfile() {
		if (_profile != null)
			return _profile;
		else
			return new RaceContainr();
	}

	private String[] getSentences(int size) {
		size = size % 2 == 1 ? size + 1 : size;
		String[] sentences = new String[size];
		for (int i = 0; i < size / 2; i++) {
			sentences[i] = _statGen.generate();
			sentences[i + size / 2] = _rudeGen.generate();
		}

		return sentences;
	}

	private RaceContainr getProfile(PImage im, Race race, int x, int y,
			int rad, int numSentences) {
		RaceContainr rc = new RaceContainr(im, crop(im, x, y, rad),
				getSentences(numSentences), race); // TODO: Replace with label
		// of the race
		return rc;
	}

	private PImage crop(PImage im, int x, int y, int rad) {
		// Get the pixel data directly:
		PImage ret = im.get(x-rad,y-rad,2*rad,2*rad);
		
		for (int i = 0; i < 2*rad; i++)
		{
			for (int j = 0; j < 2*rad; j++)
			{
				// If the pixel at i,j of the smaller jpg is within a radius of rad
				// then do nothing (leave the pixel), otherwise set the pixel to
				// white.  This will have aliased edges, but we will draw a circle
				// over the border when it is displayed.
				if (Math.pow(i-rad,2)+Math.pow(j-rad,2) < Math.pow(rad,2))
				{
					
					//ret.pixels[i*2*rad + j] = 0;
				}
				else
				{
					ret.pixels[i*2*rad + j] = _parent.color(255,255,255);
				}
			}
		}
		// This is a debugging feature, this shouldn't save in the production code:
		ret.save("../data/cropped.jpg");
		return ret;
	}

	private Color getColor(String filename, int rad, int x, int y) {
		int[] total = { 0, 0, 0 };
		int count = 0;
		BufferedImage img = null;

		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		for (int i = Math.max(0, y - (rad / 2)); i < Math.min(img.getHeight(),
				y + (rad / 2)); i++) {
			for (int j = Math.max(0, x - (rad / 2)); j < Math.min(img
					.getWidth(), x + (rad / 2)); j++) {
				double d = Math.sqrt(Math.pow(x - j, 2) + Math.pow(y - i, 2));
				if (d <= rad) {
					count++;
					int c = img.getRGB(i, j);
					total[0] += ((c << 4) & 0x0000FF) + _parent.random(-5,5);
					total[1] += ((c << 2) & 0x0000FF) + _parent.random(-5,5);
					total[2] += (c & 0x0000FF) + _parent.random(-5,5);
				}
			}
		}
		total[0] /= count;
		total[1] /= count;
		total[2] /= count;
		return new Color(total[0], total[1], total[2]);
	}

	private Race getRace(String im, int x, int y, int r) {
		return getRace(getColor(im, r, x, y));
	}

	private Race getRace(Color c) {
		// TODO: Make it harder to become white
		Race[] races = Race.values();
		double minDist = Integer.MAX_VALUE;
		Race r = Race.CAUCASIAN;
		
		for (int i = 0; i < races.length; i++) {
			double d = compareColors(c, _averageColors.get(races[i]));
			// If caucasian, then rejection sample:
			
			boolean caucasianAccept = (races[i]==Race.CAUCASIAN) ? 
					(_parent.random(1)<CAUCASIAN_SCALING?true:false) : true;
					
			if ((d <= minDist)&&caucasianAccept) {
				minDist = d;
				r = races[i];
			}
		}

		return r;
	}

	private double compareColors(Color c1, Color c2) {
		return Math.sqrt(Math.pow((c1.getGreen() - c2.getGreen()), 2)
				+ Math.pow((c1.getBlue() - c2.getBlue()), 2)
				+ Math.pow((c1.getRed() - c2.getRed()), 2));
	}

	private void set_averageColors(HashMap<Race, Color> averageColors) {
		_averageColors = averageColors;
	}

	private Color getAverageColor(String dir, int rad) {
		File fDir = new File(dir);

		String[] children = fDir.list();
		if (children == null) {
			return new Color(0);
		} else {
			int r = 0;
			int g = 0;
			int b = 0;
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				String filename = children[i];
				Color c;
				if (filename.contains(".png")) {
					c = getColor(dir + System.getProperty("file.separator")
							+ children[i], rad, rad, rad);
					r += c.getRed();
					g += c.getGreen();
					b += c.getBlue();
				}
			}
			return new Color(r / children.length, g / children.length, b
					/ children.length);
		}
	}

	
	private class Sandbox extends PApplet
	{
		PFont headingFont;
		
		public Sandbox()
		{
			size(160,120);
			headingFont = loadFont("../data/TradeGothicLTStd-Bd2-16.vlw");
			textFont(headingFont);
			fill(255, 0, 0);
		}
	}

}
