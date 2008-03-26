package racemakr.profilr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PImage;
import racemakr.structure.RaceContainr;
import rita.RiMarkov;

public class Profilr {

	private RiMarkov _rudeGen, _statGen;
	private RaceContainr _profile;
	private PApplet _parent;

	public enum Race { CAUCASIAN, ASIAN, SOUTHAMERICAN, BLACK };
	private static HashMap<Race, Color> _averageColors;
	private static final int NUM_STRINGS = 10;
	
	public Profilr(PApplet parent, String filename, int x, int y, int rad, int num_sentences)
	{
		_parent = parent;
		
		// Setup our 'profile' to compare against:
		HashMap<Race, Color> averageColors = new HashMap<Race, Color>();
		averageColors.put(Race.ASIAN, new Color(101,103,95));
		averageColors.put(Race.BLACK, new Color(98,110,128));
		averageColors.put(Race.CAUCASIAN, new Color(106,110,71));
		averageColors.put(Race.SOUTHAMERICAN, new Color(101,106,99));
		set_averageColors(averageColors);
		
		// Figure out what the race is:
		Race r = getRace(filename, rad, x, y);
		
		_rudeGen = new RiMarkov(parent, num_sentences); 
		_rudeGen.loadFile("text/" + r.name() + "_RUDE.txt");
		
		_statGen = new RiMarkov(parent, num_sentences); 
		_statGen.loadFile("text/" + r.name() + "_STATISTICS.txt");
		
		_profile = getProfile(parent.loadImage(filename),r,x,y,rad,NUM_STRINGS);
	}

	public RaceContainr getProfile()
	{
		if (_profile != null)
			return _profile;
		else
			return new RaceContainr();
	}
	
	private String[] getSentences(int size)
	{
		size = size%2==1?size+1:size;
		String[] sentences = new String[size];
		for (int i = 0; i < size/2; i++)
		{
			sentences[i] = _statGen.generate();
			sentences[i+size/2] = _rudeGen.generate();
		}
		
		return sentences;
	}
	
	private RaceContainr getProfile(PImage im, Race race, int x, int y, int rad, int numSentences)
	{
		RaceContainr rc = new RaceContainr(
				im,
				crop(im,x,y,rad),
				getSentences(numSentences),
				race); // TODO: Replace with label of the race
		return rc;
	}
	
	private PImage crop(PImage im, int x, int y, int rad) {
		PImage ret = im.get(x-rad,y-rad,2*rad,2*rad);
		int[] mask = new int[(int)(Math.pow(2*rad,2))];
		
		for (int i = 0; i < 2*rad; i++)
		{
			for (int j = 0; j < 2*rad; j++)
			{
				if (Math.pow(i,2)+Math.pow(j,2) < Math.pow(rad,2))
				{
					mask[i*2*rad + j] = 0;
				}
				else
				{
					mask[i*2*rad + j] = 255;
				}
			}
		}
		ret.mask(mask);
		
		return ret;
	}
	
	private Color getColor(String filename, int rad, int x, int y)
	{
		int[] total = {0,0,0};
		int count = 0;
		BufferedImage img = null;
		
		try
		{
			img = ImageIO.read(new File(filename));
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		for (int i = Math.max(0,y-(rad/2)); i < Math.min(img.getHeight(), y+(rad/2));i++)
		{
			for (int j = Math.max(0,x-(rad/2)); j < Math.min(img.getWidth(), x+(rad/2));j++)
			{
				double d = Math.sqrt(Math.pow(x-j,2) + Math.pow(y-i,2));
				if (d <= rad)
				{
					count++;
					int c = img.getRGB(i,j);
					total[0] += (c << 4) & 0x0000FF;
					total[1] += (c << 2) & 0x0000FF;
					total[2] +=  c       & 0x0000FF;
				}
			}
		}
		total[0]/=count;
		total[1]/=count;
		total[2]/=count;
		return new Color(total[0],total[1],total[2]);
	}
	
	private Race getRace(String im, int x, int y, int r)
	{
		return getRace(getColor(im, r, x, y));
	}
	
	private Race getRace(Color c)
	{
		// TODO: Make it harder to become white
		Race[] races = Race.values();
		double minDist = Integer.MAX_VALUE;
		Race r = Race.CAUCASIAN;
		
		for (int i = 0; i < races.length; i++)
		{
			double d = compareColors(c, _averageColors.get(races[i]));
			if (d<=minDist)
			{
				minDist = d;
				r = races[i];
			}
		}
		
		return r;
	}

	private double compareColors(Color c1, Color c2)
	{
		return Math.sqrt(Math.pow((c1.getGreen() - c2.getGreen()),2) +
				Math.pow((c1.getBlue() - c2.getBlue()),2) +
				Math.pow((c1.getRed() - c2.getRed()),2));
	}

	private void set_averageColors(HashMap<Race, Color> averageColors) {
		_averageColors = averageColors;
	}
	
	private Color getAverageColor(String dir, int rad)
	{
		File fDir = new File(dir); 

		String[] children = fDir.list(); 
		if (children == null) { 
			return new Color(0);
		} else { 
			int r=0;
			int g=0;
			int b=0;
			for (int i=0; i<children.length; i++) { 
				// Get filename of file or directory 
				String filename = children[i]; 
				Color c;
				if (filename.contains(".png"))
				{
					c=getColor(dir + System.getProperty("file.separator") + children[i], rad, rad, rad);
					r+=c.getRed();
					g+=c.getGreen();
					b+=c.getBlue();
				}
			}
			return new Color(r/children.length,g/children.length,b/children.length);
		} 
	}
	
	public static void main(String [ ] args)
	{
		// Generate the sample colors based on our sample images for each race:
		Profilr p = new Profilr(new PApplet(), "bin/capture1.jpg", 640/2, 480/2, 75, 10);
		System.out.println("Asian: " + p.getAverageColor("bin/sd/asian/", 250));
		System.out.println("Caucasian: " + p.getAverageColor("bin/sd/caucasian/", 250));
		System.out.println("Black: " + p.getAverageColor("bin/sd/black/", 250));
		System.out.println("Latin: " + p.getAverageColor("bin/sd/latin/", 250));
	}
	
}
