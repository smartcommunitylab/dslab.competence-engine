package it.smartcommunitylab.scoengine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.smartcommunitylab.scoengine.model.esco.ResourceLink;

public class TextDoc {
	private Map<String, String> fields = new HashMap<String, String>();
	private List<ResourceLink> hiearchy = new ArrayList<ResourceLink>();
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

	public List<ResourceLink> getHiearchy() {
		return hiearchy;
	}

	public void setHiearchy(List<ResourceLink> hiearchy) {
		this.hiearchy = hiearchy;
	}

}