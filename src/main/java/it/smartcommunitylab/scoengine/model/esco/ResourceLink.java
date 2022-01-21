package it.smartcommunitylab.scoengine.model.esco;

import java.util.HashMap;
import java.util.Map;

public class ResourceLink {
	private Map<String, String> preferredLabel = new HashMap<>();
	private String uri;
	private String conceptType;

	public Map<String, String> getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(Map<String, String> preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getConceptType() {
		return conceptType;
	}

	public void setConceptType(String conceptType) {
		this.conceptType = conceptType;
	}
}
