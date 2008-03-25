package racemakr;

import processing.core.PApplet;
import processing.core.PImage;
import racemakr.RaceDeterminr.Race;
import rita.*;

public class Profilr {

	private RiMarkov _rudeGen, _statGen;

	public Profilr(PApplet parent, RaceDeterminr.Race race, int n)
	{
		_rudeGen = new RiMarkov(parent, n); 
		_rudeGen.loadFile("text/" + race.name() + "_RUDE.txt");
		
		_statGen = new RiMarkov(parent, n); 
		_statGen.loadFile("text/" + race.name() + "_STATISTICS.txt");
	}
	
	public String[] getSentences(int size)
	{
		size = size%2==1?size+1:size;
		String[] sentences = new String[size];
		for (int i = 0; i < size; i+=2)
		{
			sentences[i] = _rudeGen.generate();
			sentences[i+1] = _statGen.generate();
		}
		
		return sentences;
	}
	
	public RaceContainr getRace(PImage im, int x, int y, int rad, int numSentences)
	{
		RaceContainr rc = new RaceContainr();
		rc.set_cropped(crop(im,x,y,rad));
		rc.set_label(new String());
		rc.set_original(im);
		rc.set_sentences(getSentences(numSentences));
		return rc;
	}
	
	private PImage crop(PImage im, int x, int y, int rad) {
		PImage ret = new PImage(2*rad,2*rad);
		im.copy(ret, x, y, x+2*rad, y+2*rad, 0, 0, 2*rad, 2*rad);
		
		int[] mask = new int[4*rad^2];
		
		for (int i = 0; i < 2*rad; i++)
		{
			for (int j = 0; j < 2*rad; j++)
			{
				if (Math.sqrt(i^2+j^2) < rad)
				{
					mask[i*2*rad + j] = 0;
				}
				mask[i*2*rad + j] = 255;
			}
		}
		
		im.mask(mask);
		
		return im;
	}

	public static void main(String[] args) {
		String[] s = (new Profilr(new PApplet(),Race.ASIAN,4)).getSentences(8);
		for (int i = 0; i < s.length; i++)
		{
			System.out.println(s[i]);
		}
	}

}
