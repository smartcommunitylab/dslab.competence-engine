package it.smartcommunitylab.scoengine.model.esco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class Skill {
	@Id
	private String uri;
	private String conceptType;
	private Map<String, String> preferredLabel = new HashMap<>();
	private Map<String, String> altLabels = new HashMap<>();
	private Map<String, String> description = new HashMap<>();
	private List<String> isEssentialSkill = new ArrayList<>();
	private List<ResourceLink> isEssentialSkillLink = new ArrayList<>();
	private List<String> isOptionalSkill = new ArrayList<>();
	private List<ResourceLink> isOptionalSkillLink = new ArrayList<>();
	private List<String> broaderSkill = new ArrayList<>();
	private List<ResourceLink> broaderSkillLink = new ArrayList<>();
	private List<String> narrowerSkill = new ArrayList<>();
	private List<ResourceLink> narrowerSkillLink = new ArrayList<>();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Skill)) {
			return false;
		}
		Skill skill = (Skill) obj;
		return this.uri.equals(skill.getUri());
	}

	@Override
	public int hashCode() {
		return this.uri.hashCode();
	}

	public String getConceptType() {
		return conceptType;
	}

	public void setConceptType(String conceptType) {
		this.conceptType = conceptType;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<String> getBroaderSkill() {
		return broaderSkill;
	}

	public void setBroaderSkill(List<String> broaderSkill) {
		this.broaderSkill = broaderSkill;
	}

	public List<String> getNarrowerSkill() {
		return narrowerSkill;
	}

	public void setNarrowerSkill(List<String> narrowerSkill) {
		this.narrowerSkill = narrowerSkill;
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

	public List<ResourceLink> getBroaderSkillLink() {
		return broaderSkillLink;
	}

	public void setBroaderSkillLink(List<ResourceLink> broaderSkillLink) {
		this.broaderSkillLink = broaderSkillLink;
	}

	public List<ResourceLink> getNarrowerSkillLink() {
		return narrowerSkillLink;
	}

	public void setNarrowerSkillLink(List<ResourceLink> narrowerSkillLink) {
		this.narrowerSkillLink = narrowerSkillLink;
	}

	public List<String> getIsEssentialSkill() {
		return isEssentialSkill;
	}

	public void setIsEssentialSkill(List<String> isEssentialSkill) {
		this.isEssentialSkill = isEssentialSkill;
	}

	public List<ResourceLink> getIsEssentialSkillLink() {
		return isEssentialSkillLink;
	}

	public void setIsEssentialSkillLink(List<ResourceLink> isEssentialSkillLink) {
		this.isEssentialSkillLink = isEssentialSkillLink;
	}

	public List<String> getIsOptionalSkill() {
		return isOptionalSkill;
	}

	public void setIsOptionalSkill(List<String> isOptionalSkill) {
		this.isOptionalSkill = isOptionalSkill;
	}

	public List<ResourceLink> getIsOptionalSkillLink() {
		return isOptionalSkillLink;
	}

	public void setIsOptionalSkillLink(List<ResourceLink> isOptionalSkillLink) {
		this.isOptionalSkillLink = isOptionalSkillLink;
	}

}
