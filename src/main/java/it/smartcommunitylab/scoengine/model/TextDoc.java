package it.smartcommunitylab.scoengine.model;

import java.util.HashMap;
import java.util.Map;

public class TextDoc {
	private Map<String, String> fields = new HashMap<String, String>();
	private float score;
	
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public Map<String, String> getFields() {
		return fields;
	}
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
}