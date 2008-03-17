package racemakr;

import processing.core.PApplet;
import racemakr.RaceMakr.Race;
import rita.*;

public class ProfileMaker {

	private RiMarkov _rudeGen, _statGen;

	public ProfileMaker(PApplet parent, RaceMakr.Race race, int n)
	{
		_rudeGen = new RiMarkov(parent, n); 
		_rudeGen.loadFile("text/" + race.name() + "_RUDE.txt");
		
		_statGen = new RiMarkov(parent, n); 
		_statGen.loadFile("text/" + race.name() + "_STATISTICS.txt");
	}
	
	public String[] getProfile(int size)
	{
		size = size%2==1?size+1:size;
		String[] sentences = new String[size];
		for (int i = 0; i < size/2; i++)
		{
			sentences[i] = _rudeGen.generate();
			sentences[i+1] = _statGen.generate();
		}
		
		return sentences;
	}
	
	public static void main(String[] args) {
		String[] s = (new ProfileMaker(new PApplet(),Race.ASIAN,2)).getProfile(8);
		for (int i = 0; i < s.length; i++)
		{
			System.out.println(s);
		}
	}

}
