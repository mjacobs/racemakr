package racemakr;

import processing.core.PApplet;
import racemakr.RaceMakr.Race;
import rita.*;

public class ProfileMaker {

	private RiMarkov _markovModel;

	public ProfileMaker(PApplet parent, RaceMakr.Race race, int n)
	{
		_markovModel = new RiMarkov(parent, n); 
		_markovModel.loadFile(race.name() + ".txt");
	}
	
	public String getProfile(int size)
	{
		int wc = 0;
		String ret = new String();
		while (wc < size)
		{
			ret += (wc==0?"  ":"") + _markovModel.generate();
		}
		return ret;
	}
	
	public static void main(String[] args) {
		
	}

}
