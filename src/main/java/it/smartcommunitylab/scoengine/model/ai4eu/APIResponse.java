package it.smartcommunitylab.scoengine.model.ai4eu;

import java.util.ArrayList;
import java.util.List;

public class APIResponse {
	List<Resource> values_tfidf = new ArrayList<>();
	List<Resource> values_sent = new ArrayList<>();
	List<Resource> values_bert = new ArrayList<>();
	String[] multipliers;
	String[] tokens;

	public List<Resource> getValues_tfidf() {
		return values_tfidf;
	}

	public void setValues_tfidf(List<Resource> values_tfidf) {
		this.values_tfidf = values_tfidf;
	}

	public List<Resource> getValues_sent() {
		return values_sent;
	}

	public void setValues_sent(List<Resource> values_sent) {
		this.values_sent = values_sent;
	}

	public List<Resource> getValues_bert() {
		return values_bert;
	}

	public void setValues_bert(List<Resource> values_bert) {
		this.values_bert = values_bert;
	}

	public String[] getMultipliers() {
		return multipliers;
	}

	public void setMultipliers(String[] multipliers) {
		this.multipliers = multipliers;
	}

	public String[] getTokens() {
		return tokens;
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}

}
