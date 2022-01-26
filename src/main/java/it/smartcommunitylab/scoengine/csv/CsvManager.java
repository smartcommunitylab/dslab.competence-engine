package it.smartcommunitylab.scoengine.csv;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.scoengine.common.Const;
import it.smartcommunitylab.scoengine.lucene.LuceneManager;
import it.smartcommunitylab.scoengine.model.TextDoc;
import it.smartcommunitylab.scoengine.model.esco.Level;
import it.smartcommunitylab.scoengine.model.esco.ResourceLink;
import it.smartcommunitylab.scoengine.model.esco.Skill;
import it.smartcommunitylab.scoengine.model.esco.SkillGroup;
import it.smartcommunitylab.scoengine.repository.SkillGroupRepository;
import it.smartcommunitylab.scoengine.repository.SkillRepository;

@Component
public class CsvManager {
	private static final transient Logger logger = LoggerFactory.getLogger(CsvManager.class);

	@Autowired
	private LuceneManager luceneManager;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private SkillGroupRepository skillGroupRepository;

	public void indexSkills(String csvFilePath) throws IOException {
		List<Document> docs = new ArrayList<Document>();
		Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVFormat csvFormat = CSVFormat.DEFAULT
				.withHeader("conceptType", "conceptUri", "skillType", "reuseLevel", "preferredLabel", "altLabels",
						"hiddenLabels", "status", "modifiedDate", "scopeNote", "definition", "inScheme", "description")
				.withSkipHeaderRecord();
		CSVParser csvParser = new CSVParser(reader, csvFormat);
		for (CSVRecord record : csvParser) {
			Document doc = new Document();
			doc.add(new Field("uri", record.get("conceptUri"), TextField.TYPE_STORED));
			doc.add(new Field("conceptType", record.get("conceptType"), TextField.TYPE_STORED));
			doc.add(new Field("reuseLevel", record.get("reuseLevel"), TextField.TYPE_STORED));
			doc.add(new Field("preferredLabel", record.get("preferredLabel"), TextField.TYPE_STORED));
			doc.add(new Field("altLabels", record.get("altLabels"), TextField.TYPE_STORED));
			doc.add(new Field("reuseLevel", record.get("reuseLevel"), TextField.TYPE_STORED));
			doc.add(new Field("description", record.get("description"), TextField.TYPE_STORED));
			doc.add(new Field("preferredLabelNormalized", luceneManager.normalizeText(record.get("preferredLabel")),
					TextField.TYPE_STORED));
			doc.add(new Field("altLabelsNormalized", luceneManager.normalizeText(record.get("altLabels")),
					TextField.TYPE_STORED));
			doc.add(new Field("descriptionNormalized", luceneManager.normalizeText(record.get("description")),
					TextField.TYPE_STORED));
			docs.add(doc);
		}
		if (docs.size() > 0) {
			luceneManager.indexDocuments(docs);
		}
		csvParser.close();
		reader.close();
		logger.info("indexSkills:{}", docs.size());
	}

	public List<TextDoc> getByUri(String field, String fieldTitle, int maxResult) throws Exception {
		return luceneManager.searchBySingleField(field, fieldTitle, maxResult);
	}

	public void importSkills(String csvFilePath, String lang) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVFormat csvFormat = CSVFormat.DEFAULT
				.withHeader("conceptType", "conceptUri", "skillType", "reuseLevel", "preferredLabel", "altLabels",
						"hiddenLabels", "status", "modifiedDate", "scopeNote", "definition", "inScheme", "description")
				.withSkipHeaderRecord();
		CSVParser csvParser = new CSVParser(reader, csvFormat);
		for (CSVRecord record : csvParser) {
			String uri = record.get("conceptUri");
			String conceptType = record.get("conceptType");
			String preferredLabel = record.get("preferredLabel");
			String altLabels = record.get("altLabels");
			String description = record.get("description");
			String reuseLevel = record.get("reuseLevel");
			Skill skill = null;
			Optional<Skill> optionalSkill = skillRepository.findById(uri);
			if (optionalSkill.isEmpty()) {
				skill = new Skill();
				skill.setUri(uri);
				skill.setConceptType(conceptType);
				skill.setReuseLevel(reuseLevel);
			} else {
				skill = optionalSkill.get();
			}
			skill.getPreferredLabel().put(lang, preferredLabel);
			skill.getAltLabels().put(lang, altLabels);
			skill.getDescription().put(lang, description);
			skillRepository.save(skill);
			logger.info("importSkills:{}/{}", lang, uri);
		}
		csvParser.close();
		reader.close();
	}

	public void importSkillEssentialRelations(String csvFilePath) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("originalSkillUri", "originalSkillType", "relationType",
				"relatedSkillType", "relatedSkillUri").withSkipHeaderRecord();
		CSVParser csvParser = new CSVParser(reader, csvFormat);
		for (CSVRecord record : csvParser) {
			String originalSkillUri = record.get("originalSkillUri");
			Optional<Skill> optional = skillRepository.findById(originalSkillUri);
			if (optional.isEmpty()) {
				logger.info("originalSkill not found:{}", originalSkillUri);
			} else {
				Skill originalSkill = optional.get();
				String relatedSkillUri = record.get("relatedSkillUri");
				String relationType = record.get("relationType");
				Optional<Skill> optionalSkill = skillRepository.findById(relatedSkillUri);
				if (optionalSkill.isPresent()) {
					Skill relatedSkill = optionalSkill.get();
					if ("essential".equals(relationType)) {
						if (!originalSkill.getEssentialSkill().contains(relatedSkillUri)) {
							originalSkill.getEssentialSkill().add(relatedSkillUri);
							ResourceLink rLink = new ResourceLink();
							rLink.setPreferredLabel(relatedSkill.getPreferredLabel());
							rLink.setUri(relatedSkill.getUri());
							rLink.setConceptType(relatedSkill.getConceptType());
							originalSkill.getEssentialSkillLinks().add(rLink);
							skillRepository.save(originalSkill);
							// essentialLinkOf
							if (!relatedSkill.getEssentialSkillOf().contains(originalSkillUri)) {
								relatedSkill.getEssentialSkillOf().add(originalSkillUri);
								ResourceLink rLinkOf = new ResourceLink();
								rLinkOf.setPreferredLabel(originalSkill.getPreferredLabel());
								rLinkOf.setUri(originalSkill.getUri());
								rLinkOf.setConceptType(originalSkill.getConceptType());
								relatedSkill.getEssentialSkillOfLinks().add(rLinkOf);
								skillRepository.save(relatedSkill);
							}

						}
					} else {
						if (!originalSkill.getOptionalSkill().contains(relatedSkillUri)) {
							originalSkill.getOptionalSkill().add(relatedSkillUri);
							ResourceLink rLink = new ResourceLink();
							rLink.setPreferredLabel(relatedSkill.getPreferredLabel());
							rLink.setUri(relatedSkill.getUri());
							rLink.setConceptType(relatedSkill.getConceptType());
							originalSkill.getOptionalSkillLink().add(rLink);
							skillRepository.save(originalSkill);
							// optionalLinkOf
							if (!relatedSkill.getOptionalSkillOf().contains(originalSkillUri)) {
								relatedSkill.getOptionalSkillOf().add(originalSkillUri);
								ResourceLink rLinkOf = new ResourceLink();
								rLinkOf.setPreferredLabel(originalSkill.getPreferredLabel());
								rLinkOf.setUri(originalSkill.getUri());
								rLinkOf.setConceptType(originalSkill.getConceptType());
								relatedSkill.getOptionalSkillOfLink().add(rLinkOf);
								skillRepository.save(relatedSkill);
							}
						}
					}
				}
				logger.info("importSkillEssentialRelations:{}", originalSkillUri);
			}
		}
		csvParser.close();
		reader.close();
	}

	public void importSkillBroaderRelation(String csvFilePath) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("conceptType", "conceptUri", "broaderType", "broaderUri")
				.withSkipHeaderRecord();
		CSVParser csvParser = new CSVParser(reader, csvFormat);
		for (CSVRecord record : csvParser) {
			String conceptType = record.get("conceptType");
			String conceptUri = record.get("conceptUri");
			String broaderConceptType = record.get("broaderType");
			String broaderConceptUri = record.get("broaderUri");

			if (conceptType.equals(Const.ESCO_CONCEPT_SKILL_GROUP)) {
				// SkillGroup, SkillGroup
				Optional<SkillGroup> conceptSkillGroupOptional = skillGroupRepository.findById(conceptUri);
				if (!conceptSkillGroupOptional.isEmpty()) {
					SkillGroup conceptSkillGroup = conceptSkillGroupOptional.get();
					Optional<SkillGroup> broaderSkillGroupOptional = skillGroupRepository.findById(broaderConceptUri);
					if (!broaderSkillGroupOptional.isEmpty()) {
						SkillGroup broaderSkillGroup = broaderSkillGroupOptional.get();
						if (!conceptSkillGroup.getBroaderSkill().contains(broaderConceptUri)) {
							conceptSkillGroup.getBroaderSkill().add(broaderConceptUri);
							ResourceLink rLinkGroup = new ResourceLink();
							rLinkGroup.setPreferredLabel(broaderSkillGroup.getPreferredLabel());
							rLinkGroup.setUri(broaderSkillGroup.getUri());
							rLinkGroup.setConceptType(broaderSkillGroup.getConceptType());
							conceptSkillGroup.getBroaderSkillLink().add(rLinkGroup);
							skillGroupRepository.save(conceptSkillGroup);
						}
						if (!broaderSkillGroup.getNarrowerSkill().contains(conceptUri)) {
							broaderSkillGroup.getNarrowerSkill().add(conceptUri);
							ResourceLink rLinkGroup = new ResourceLink();
							rLinkGroup.setPreferredLabel(conceptSkillGroup.getPreferredLabel());
							rLinkGroup.setUri(conceptSkillGroup.getUri());
							rLinkGroup.setConceptType(conceptSkillGroup.getConceptType());
							broaderSkillGroup.getNarrowerSkillLink().add(rLinkGroup);
							skillGroupRepository.save(broaderSkillGroup);
						}
						logger.info("Import SkillGroup-SkillGroup broader relation:{}/{}", conceptUri,
								broaderConceptUri);
					}
				} else {
					logger.info("Skipping SkillGroup-SkillGroup broader relation:{}/{}", conceptUri, broaderConceptUri);
				}
			} else if (conceptType.equals(Const.ESCO_CONCEPT_SKILL)
					&& broaderConceptType.equals(Const.ESCO_CONCEPT_SKILL_GROUP)) {
				// Skill, SkillGroup
				Optional<Skill> optionalSkill = skillRepository.findById(conceptUri);
				if (!optionalSkill.isEmpty()) {
					Skill skill = optionalSkill.get();
					if (!skill.getBroaderSkill().contains(broaderConceptUri)) {
						skill.getBroaderSkill().add(broaderConceptUri);
						Optional<SkillGroup> skillGroupOptional = skillGroupRepository.findById(broaderConceptUri);
						if (!skillGroupOptional.isEmpty()) {
							SkillGroup skillGroup = skillGroupOptional.get();
							ResourceLink rLink = new ResourceLink();
							rLink.setUri(broaderConceptUri);
							rLink.setConceptType(broaderConceptType);
							rLink.setPreferredLabel(skillGroup.getPreferredLabel());
							skill.getBroaderSkillLink().add(rLink);
							skillRepository.save(skill);
							if (!skillGroup.getNarrowerSkill().contains(conceptUri)) {
								skillGroup.getNarrowerSkill().add(conceptUri);
								ResourceLink rLinkGroup = new ResourceLink();
								rLinkGroup.setUri(conceptUri);
								rLinkGroup.setConceptType(skill.getConceptType());
								rLinkGroup.setPreferredLabel(skill.getPreferredLabel());
								skillGroup.getNarrowerSkillLink().add(rLinkGroup);
								skillGroupRepository.save(skillGroup);
							}
							logger.info("Import Skill-SkillGroup broader relation:{}/{}", conceptUri,
									broaderConceptUri);
						}
					}
				} else {
					logger.info("Skipping Skill-SkillGroup broader relation:{}/{}", conceptUri, broaderConceptUri);
				}
			} else if (conceptType.equals(Const.ESCO_CONCEPT_SKILL)
					&& broaderConceptType.equals(Const.ESCO_CONCEPT_SKILL)) {
				// Skill, Skill
				Optional<Skill> optionalSkill = skillRepository.findById(conceptUri);
				Skill skill = optionalSkill.get();
				if (!optionalSkill.isEmpty()) {
					Optional<Skill> optionalBroaderSkill = skillRepository.findById(broaderConceptUri);
					Skill broaderSkill = optionalBroaderSkill.get();
					if (!optionalBroaderSkill.isEmpty()) {
						if (!skill.getBroaderSkill().contains(broaderConceptUri)) {
							skill.getBroaderSkill().add(broaderConceptUri);
							ResourceLink rLink = new ResourceLink();
							rLink.setPreferredLabel(broaderSkill.getPreferredLabel());
							rLink.setUri(broaderSkill.getUri());
							rLink.setConceptType(broaderSkill.getConceptType());
							skill.getBroaderSkillLink().add(rLink);
							skillRepository.save(skill);
						}
						if (!broaderSkill.getNarrowerSkill().contains(conceptUri)) {
							broaderSkill.getNarrowerSkill().add(conceptUri);
							ResourceLink rLink = new ResourceLink();
							rLink.setPreferredLabel(skill.getPreferredLabel());
							rLink.setUri(skill.getUri());
							rLink.setConceptType(skill.getConceptType());
							broaderSkill.getNarrowerSkillLink().add(rLink);
							skillRepository.save(broaderSkill);
						}
						logger.info("import Skill-Skill broader relation:{}/{}", conceptUri, broaderConceptUri);
					}
				} else {
					logger.info("Skipping Skill-Skill broader relation:{}/{}", conceptUri, broaderConceptUri);
				}
			}
		}
		csvParser.close();
		reader.close();
	}

	public void importSkillGroups(String csvFilePath, String lang) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("conceptType", "conceptUri", "preferredLabel", "altLabels",
				"hiddenLabels", "status", "modifiedDate", "scopeNote", "inScheme", "description", "code")
				.withSkipHeaderRecord();
		CSVParser csvParser = new CSVParser(reader, csvFormat);
		for (CSVRecord record : csvParser) {
			String uri = record.get("conceptUri");
			String conceptType = record.get("conceptType");
			String preferredLabel = record.get("preferredLabel");
			String altLabels = record.get("altLabels");
			String description = record.get("description");
			String code = record.get("code");
			SkillGroup skillGroup = null;
			Optional<SkillGroup> optionalSkillGroup = skillGroupRepository.findById(uri);
			if (optionalSkillGroup.isEmpty()) {
				skillGroup = new SkillGroup();
				skillGroup.setUri(uri);
				skillGroup.setConceptType(conceptType);
				skillGroup.setCode(code);
			} else {
				skillGroup = optionalSkillGroup.get();
			}
			skillGroup.getPreferredLabel().put(lang, preferredLabel);
			skillGroup.getAltLabels().put(lang, altLabels);
			skillGroup.getDescription().put(lang, description);
			skillGroupRepository.save(skillGroup);
			logger.info("importSkillGroups:{}/{}", lang, uri);
		}
		csvParser.close();
		reader.close();
	}

	public void importSkillGroupsHierarchy(String csvFilePath, String lang) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVFormat csvFormat = CSVFormat.DEFAULT
				.withHeader("Level 0 URI", "Level 0 preferred term", "Level 1 URI", "Level 1 preferred term",
						"Level 2 URI", "Level 2 preferred term", "Level 3 URI", "Level 3 preferred term", "Description",
						"Scope note", "Level 0 code", "Level 1 code", "Level 2 code", "Level 3 code")
				.withSkipHeaderRecord();
		CSVParser csvParser = new CSVParser(reader, csvFormat);
		for (CSVRecord record : csvParser) {
			String uriLevel0 = record.get("Level 0 URI");
			String preferredTermLevel0 = record.get("Level 0 preferred term");
			String codeLevel0 = record.get("Level 0 code");
			String uriLevel1 = record.get("Level 1 URI");
			String preferredTermLevel1 = record.get("Level 1 preferred term");
			String codeLevel1 = record.get("Level 1 code");
			String uriLevel2 = record.get("Level 2 URI");
			String preferredTermLevel2 = record.get("Level 2 preferred term");
			String codeLevel2 = record.get("Level 2 code");
			String uriLevel3 = record.get("Level 3 URI");
			String preferredTermLevel3 = record.get("Level 3 preferred term");
			String codeLevel3 = record.get("Level 3 code");
			String levelGroupDescription = record.get("Description");

			Optional<SkillGroup> optionalSkillGroup = skillGroupRepository.findById(uriLevel0);
			SkillGroup skillGroup = null;
			if (optionalSkillGroup.isEmpty()) {
				optionalSkillGroup = skillGroupRepository.findById(uriLevel1);
				if (optionalSkillGroup.isEmpty()) {
					optionalSkillGroup = skillGroupRepository.findById(uriLevel2);
					if (optionalSkillGroup.isEmpty()) {
						optionalSkillGroup = skillGroupRepository.findById(uriLevel3);
						if (optionalSkillGroup.isEmpty()) {
							logger.info("skip skill group hierarchy:{}", uriLevel3);
							continue;
						} else {
							skillGroup = optionalSkillGroup.get();
						}
					} else {
						skillGroup = optionalSkillGroup.get();
					}
				} else {
					skillGroup = optionalSkillGroup.get();
				}
			} else {
				skillGroup = optionalSkillGroup.get();
			}

			skillGroup.setLevelDescription(levelGroupDescription);
			if (!skillGroup.getLevels().containsKey("0")) {
				Level level0 = new Level();
				level0.setCode(codeLevel0);
				level0.setPreferredTerm(preferredTermLevel0);
				level0.setUri(uriLevel0);
				skillGroup.getLevels().put("0", level0);
				skillGroupRepository.save(skillGroup);

			}
			if (!skillGroup.getLevels().containsKey("1")) {
				Level level1 = new Level();
				level1.setCode(codeLevel1);
				level1.setPreferredTerm(preferredTermLevel1);
				level1.setUri(uriLevel1);
				skillGroup.getLevels().put("1", level1);
				skillGroupRepository.save(skillGroup);
			}
			if (!skillGroup.getLevels().containsKey("2")) {
				Level level2 = new Level();
				level2.setCode(codeLevel2);
				level2.setPreferredTerm(preferredTermLevel2);
				level2.setUri(uriLevel2);
				skillGroup.getLevels().put("2", level2);
				skillGroupRepository.save(skillGroup);
			}
			if (!skillGroup.getLevels().containsKey("3")) {
				Level level3 = new Level();
				level3.setCode(codeLevel3);
				level3.setPreferredTerm(preferredTermLevel3);
				level3.setUri(uriLevel3);
				skillGroup.getLevels().put("3", level3);
				skillGroupRepository.save(skillGroup);
			}
		}
		csvParser.close();
		reader.close();
	}

}
