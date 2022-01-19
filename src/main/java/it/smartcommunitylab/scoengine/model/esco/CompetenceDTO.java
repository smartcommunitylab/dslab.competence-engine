package it.smartcommunitylab.scoengine.model.esco;

public class CompetenceDTO extends ExperienceDTO {
	private String uri;
	private String concentType;
	private String preferredLabel;
	private String altLabel;
	private String description;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getConcentType() {
		return concentType;
	}

	public void setConcentType(String concentType) {
		this.concentType = concentType;
	}

	public String getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(String preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public String getAltLabel() {
		return altLabel;
	}

	public void setAltLabel(String altLabel) {
		this.altLabel = altLabel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
