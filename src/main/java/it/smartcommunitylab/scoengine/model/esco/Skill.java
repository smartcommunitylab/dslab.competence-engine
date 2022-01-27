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
	private String reuseLevel;
	private Map<String, String> preferredLabel = new HashMap<>();
	private Map<String, String> altLabels = new HashMap<>();
	private Map<String, String> description = new HashMap<>();
	private List<String> essentialSkill = new ArrayList<>();
	private List<ResourceLink> essentialSkillLinks = new ArrayList<>();
	private List<String> optionalSkill = new ArrayList<>();
	private List<ResourceLink> optionalSkillLink = new ArrayList<>();
	private List<String> essentialSkillOf = new ArrayList<>();
	private List<ResourceLink> essentialSkillOfLinks = new ArrayList<>();
	private List<String> optionalSkillOf = new ArrayList<>();
	private List<ResourceLink> optionalSkillOfLink = new ArrayList<>();
	private List<String> broaderSkill = new ArrayList<>();
	private List<ResourceLink> broaderSkillLink = new ArrayList<>();
	private List<String> narrowerSkill = new ArrayList<>();
	private List<ResourceLink> narrowerSkillLink = new ArrayList<>();
	private List<String> inScheme = new ArrayList<>();

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

	public String getReuseLevel() {
		return reuseLevel;
	}

	public void setReuseLevel(String reuseLevel) {
		this.reuseLevel = reuseLevel;
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

	public List<String> getEssentialSkill() {
		return essentialSkill;
	}

	public void setEssentialSkill(List<String> essentialSkill) {
		this.essentialSkill = essentialSkill;
	}

	public List<ResourceLink> getEssentialSkillLinks() {
		return essentialSkillLinks;
	}

	public void setEssentialSkillLinks(List<ResourceLink> essentialSkillLinks) {
		this.essentialSkillLinks = essentialSkillLinks;
	}

	public List<String> getOptionalSkill() {
		return optionalSkill;
	}

	public void setOptionalSkill(List<String> optionalSkill) {
		this.optionalSkill = optionalSkill;
	}

	public List<ResourceLink> getOptionalSkillLink() {
		return optionalSkillLink;
	}

	public void setOptionalSkillLink(List<ResourceLink> optionalSkillLink) {
		this.optionalSkillLink = optionalSkillLink;
	}

	public List<String> getEssentialSkillOf() {
		return essentialSkillOf;
	}

	public void setEssentialSkillOf(List<String> essentialSkillOf) {
		this.essentialSkillOf = essentialSkillOf;
	}

	public List<ResourceLink> getEssentialSkillOfLinks() {
		return essentialSkillOfLinks;
	}

	public void setEssentialSkillOfLinks(List<ResourceLink> essentialSkillOfLinks) {
		this.essentialSkillOfLinks = essentialSkillOfLinks;
	}

	public List<String> getOptionalSkillOf() {
		return optionalSkillOf;
	}

	public void setOptionalSkillOf(List<String> optionalSkillOf) {
		this.optionalSkillOf = optionalSkillOf;
	}

	public List<ResourceLink> getOptionalSkillOfLink() {
		return optionalSkillOfLink;
	}

	public void setOptionalSkillOfLink(List<ResourceLink> optionalSkillOfLink) {
		this.optionalSkillOfLink = optionalSkillOfLink;
	}

	public List<String> getInScheme() {
		return inScheme;
	}

	public void setInScheme(List<String> inScheme) {
		this.inScheme = inScheme;
	}

}
