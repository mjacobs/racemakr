package racemakr;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class RaceDeterminr {

	public enum Race { CAUCASIAN, ASIAN, SOUTHAMERICAN, BLACK };
	private static HashMap<Race, Color> _averageColors;
	
	public static Color getColor(String filename, int rad, int x, int y)
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
		System.out.println(total[0]+","+total[1]+","+total[2]);
		return new Color(total[0],total[1],total[2]);
	}
	
	public static Race getRace(Color c)
	{
		// TODO: Make it harder to become white
		Race[] races = Race.values();
		double minDist = (double)Integer.MAX_VALUE;
		Race r = Race.CAUCASIAN;
		
		for (int i = 0; i < races.length; i++)
		{
			double d = compareColors(c, _averageColors.get(races[i]));
			r = (d<=minDist)?races[i]:r;
		}
		
		return r;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RaceDeterminr.getColor("ostrich.jpg", 100, 25, 35);
	}

	public static double compareColors(Color c1, Color c2)
	{
		return Math.sqrt(Math.pow((c1.getGreen() - c2.getGreen()),2) +
				Math.pow((c1.getBlue() - c2.getBlue()),2) +
				Math.pow((c1.getRed() - c2.getRed()),2));
	}
	
}
