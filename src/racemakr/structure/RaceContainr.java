package racemakr.datastructs;

import processing.core.PApplet;
import processing.core.PImage;
import racemakr.profilr.Profilr;

public class RaceContainr {

	private PImage _cropped, _original;
	private String[] _sentences;
	private Profilr.Race _race;
	private String _label;
	
	public RaceContainr()
	{
		
	}
	
	public RaceContainr (PImage c, PImage o, String[] s, Profilr.Race r)
	{
		_cropped = c;
		_original = o;
		_race = r;
		_sentences = s;
		_label = r.toString().toLowerCase();
	}
	
	public RaceContainr (PImage c, PImage o, String[] s, Profilr.Race r, String l)
	{
		_cropped = c;
		_original = o;
		_race = r;
		_sentences = s;
		_label = l;
	}

	public void print() {
		System.out.println("The individual is " + _race.toString());
		for (int i = 0; i < _sentences.length; i++)
			System.out.println(_sentences[i]);
	}

	public PImage get_cropped() {
		return _cropped;
	}

	public void set_cropped(PImage _cropped) {
		this._cropped = _cropped;
	}

	public PImage get_original() {
		return _original;
	}

	public void set_original(PImage _original) {
		this._original = _original;
	}

	public String[] get_sentences() {
		return _sentences;
	}

	public void set_sentences(String[] _sentences) {
		this._sentences = _sentences;
	}

	public Profilr.Race get_race() {
		return _race;
	}

	public void set_race(Profilr.Race _race) {
		this._race = _race;
	}

	public String get_label() {
		return _label;
	}

	public void set_label(String _label) {
		this._label = _label;
	}
	
}
