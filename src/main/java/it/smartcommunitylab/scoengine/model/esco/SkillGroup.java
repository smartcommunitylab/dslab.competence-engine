package it.smartcommunitylab.scoengine.model.esco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class SkillGroup {
	@Id
	private String uri;
	private String conceptType;
	private Map<String, String> preferredLabel = new HashMap<>();
	private Map<String, String> altLabels = new HashMap<>();
	private Map<String, String> description = new HashMap<>();
	private String code;
	private Map<String, Level> levels = new HashMap<>();
	private String levelDescription;
	private List<String> broaderSkill = new ArrayList<>();
	private List<ResourceLink> broaderSkillLink = new ArrayList<>();
	private List<String> narrowerSkill = new ArrayList<>();
	private List<ResourceLink> narrowerSkillLink = new ArrayList<>();

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

	public Map<String, String> getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(Map<String, String> preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public Map<String, String> getAltLabels() {
		return altLabels;
	}

	public void setAltLabels(Map<String, String> altLabels) {
		this.altLabels = altLabels;
	}

	public Map<String, String> getDescription() {
		return description;
	}

	public void setDescription(Map<String, String> description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, Level> getLevels() {
		return levels;
	}

	public void setLevels(Map<String, Level> levels) {
		this.levels = levels;
	}

	public String getLevelDescription() {
		return levelDescription;
	}

	public void setLevelDescription(String levelDescription) {
		this.levelDescription = levelDescription;
	}

	public List<String> getBroaderSkill() {
		return broaderSkill;
	}

	public void setBroaderSkill(List<String> broaderSkill) {
		this.broaderSkill = broaderSkill;
	}

	public List<ResourceLink> getBroaderSkillLink() {
		return broaderSkillLink;
	}

	public void setBroaderSkillLink(List<ResourceLink> broaderSkillLink) {
		this.broaderSkillLink = broaderSkillLink;
	}

	public List<String> getNarrowerSkill() {
		return narrowerSkill;
	}

	public void setNarrowerSkill(List<String> narrowerSkill) {
		this.narrowerSkill = narrowerSkill;
	}

	public List<ResourceLink> getNarrowerSkillLink() {
		return narrowerSkillLink;
	}

	public void setNarrowerSkillLink(List<ResourceLink> narrowerSkillLink) {
		this.narrowerSkillLink = narrowerSkillLink;
	}

}
