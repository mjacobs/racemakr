package racemakr;

import java.util.Collection;

import processing.core.PImage;

public class RaceContainr {

	private PImage _cropped, _original;
	private String[] _sentences;
	private RaceDeterminr.Race _race;
	private String _label;
	
	public RaceContainr()
	{
		
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

	public RaceDeterminr.Race get_race() {
		return _race;
	}

	public void set_race(RaceDeterminr.Race _race) {
		this._race = _race;
	}

	public String get_label() {
		return _label;
	}

	public void set_label(String _label) {
		this._label = _label;
	}
}
