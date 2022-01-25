package it.smartcommunitylab.scoengine.model.esco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillSearchStub {
	private String uri;
	private String conceptType;
	private String reuseLevel;
	private Map<String, String> preferredLabel = new HashMap<>();
	private List<ResourceLink> hierarchy = new ArrayList<>();

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

	public String getReuseLevel() {
		return reuseLevel;
	}

	public void setReuseLevel(String reuseLevel) {
		this.reuseLevel = reuseLevel;
	}

	public Map<String, String> getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(Map<String, String> preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public List<ResourceLink> getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(List<ResourceLink> hierarchy) {
		this.hierarchy = hierarchy;
	}

}
