package it.smartcommunitylab.scoengine.model.esco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class Occupation {
	@Id
	private String uri;
	private String conceptType;
	private Map<String, String> preferredLabel = new HashMap<>();
	private Map<String, String> altLabels = new HashMap<>();
	private Map<String, String> description = new HashMap<>();
	private String broaderIscoGroup;
	private String iscoCode;
	private List<String> hasEssentialSkill = new ArrayList<String>();
	private List<String> hasOptionalSkill = new ArrayList<String>();
	private List<String> totalSkill = new ArrayList<String>();
	private List<String> narrowerOccupation = new ArrayList<String>();
	private List<String> broaderOccupation = new ArrayList<String>();
	private List<ResourceLink> hasEssentialSkillLink = new ArrayList<>();
	private List<ResourceLink> hasOptionalSkillLink = new ArrayList<>();
	private List<ResourceLink> narrowerOccupationLink = new ArrayList<>();
	private List<ResourceLink> broaderOccupationLink = new ArrayList<>();
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if (!(obj instanceof Occupation)) {
      return false;
    }
		Occupation occupation = (Occupation) obj;
		return this.uri.equals(occupation.getUri());
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
	public String getBroaderIscoGroup() {
		return broaderIscoGroup;
	}
	public void setBroaderIscoGroup(String broaderIscoGroup) {
		this.broaderIscoGroup = broaderIscoGroup;
	}
	public List<String> getHasEssentialSkill() {
		return hasEssentialSkill;
	}
	public void setHasEssentialSkill(List<String> hasEssentialSkill) {
		this.hasEssentialSkill = hasEssentialSkill;
	}
	public List<String> getHasOptionalSkill() {
		return hasOptionalSkill;
	}
	public void setHasOptionalSkill(List<String> hasOptionalSkill) {
		this.hasOptionalSkill = hasOptionalSkill;
	}
	public List<String> getNarrowerOccupation() {
		return narrowerOccupation;
	}
	public void setNarrowerOccupation(List<String> narrowerOccupation) {
		this.narrowerOccupation = narrowerOccupation;
	}
	public List<String> getBroaderOccupation() {
		return broaderOccupation;
	}
	public void setBroaderOccupation(List<String> broaderOccupation) {
		this.broaderOccupation = broaderOccupation;
	}
	public String getIscoCode() {
		return iscoCode;
	}
	public void setIscoCode(String iscoCode) {
		this.iscoCode = iscoCode;
	}

	public List<String> getTotalSkill() {
		return totalSkill;
	}

	public void setTotalSkill(List<String> totalSkill) {
		this.totalSkill = totalSkill;
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

	public List<ResourceLink> getHasEssentialSkillLink() {
		return hasEssentialSkillLink;
	}

	public void setHasEssentialSkillLink(List<ResourceLink> hasEssentialSkillLink) {
		this.hasEssentialSkillLink = hasEssentialSkillLink;
	}

	public List<ResourceLink> getHasOptionalSkillLink() {
		return hasOptionalSkillLink;
	}

	public void setHasOptionalSkillLink(List<ResourceLink> hasOptionalSkillLink) {
		this.hasOptionalSkillLink = hasOptionalSkillLink;
	}

	public List<ResourceLink> getNarrowerOccupationLink() {
		return narrowerOccupationLink;
	}

	public void setNarrowerOccupationLink(List<ResourceLink> narrowerOccupationLink) {
		this.narrowerOccupationLink = narrowerOccupationLink;
	}

	public List<ResourceLink> getBroaderOccupationLink() {
		return broaderOccupationLink;
	}

	public void setBroaderOccupationLink(List<ResourceLink> broaderOccupationLink) {
		this.broaderOccupationLink = broaderOccupationLink;
	}
}
