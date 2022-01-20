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

import it.smartcommunitylab.scoengine.lucene.LuceneManager;
import it.smartcommunitylab.scoengine.model.TextDoc;
import it.smartcommunitylab.scoengine.model.esco.ResourceLink;
import it.smartcommunitylab.scoengine.model.esco.Skill;
import it.smartcommunitylab.scoengine.repository.SkillRepository;

@Component
public class CsvManager {
	private static final transient Logger logger = LoggerFactory.getLogger(CsvManager.class);

	@Autowired
	private LuceneManager luceneManager;
	@Autowired
	private SkillRepository skillRepository;

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
			Skill skill = null;
			Optional<Skill> optionalSkill = skillRepository.findById(uri);
			if (optionalSkill.isEmpty()) {
				skill = new Skill();
				skill.setUri(uri);
				skill.setConceptType(conceptType);
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

	public void importSkillChildRelations(String csvFilePath) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVFormat csvFormat = CSVFormat.DEFAULT
				.withHeader("originalSkillUri", "relationType", "relatedSkillType", "relatedSkillUri")
				.withSkipHeaderRecord();
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
					Skill skill = optionalSkill.get();
					if ("essential".equals(relationType)) {
						if (!originalSkill.getIsEssentialForOccupation().contains(relatedSkillUri)) {
							originalSkill.getIsEssentialForOccupation().add(relatedSkillUri);
							ResourceLink rLink = new ResourceLink();
							rLink.setPreferredLabel(skill.getPreferredLabel());
							rLink.setUri(skill.getUri());
							rLink.setConceptType(skill.getConceptType());
							originalSkill.getIsEssentialForOccupationLink().add(rLink);
							skillRepository.save(skill);
						}
					} else {
						if (!originalSkill.getIsOptionalForOccupation().contains(relatedSkillUri)) {
							originalSkill.getIsOptionalForOccupation().add(relatedSkillUri);
							ResourceLink rLink = new ResourceLink();
							rLink.setPreferredLabel(skill.getPreferredLabel());
							rLink.setUri(skill.getUri());
							rLink.setConceptType(skill.getConceptType());
							originalSkill.getIsOptionalForOccupationLink().add(rLink);
							skillRepository.save(skill);
						}
					}
				}
				skillRepository.save(originalSkill);
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
			String skillUri = record.get("conceptUri");
			String broaderSkillUri = record.get("broaderUri");
			Optional<Skill> optionalSkill = skillRepository.findById(skillUri);
			Optional<Skill> optionalBroaderSkill = skillRepository.findById(broaderSkillUri);
			if (optionalSkill.isEmpty() || optionalBroaderSkill.isEmpty()) {
				logger.info("skip skill relation:{} / {}", skillUri, broaderSkillUri);
				continue;
			}
			Skill skill = optionalSkill.get();
			Skill broaderSkill = optionalBroaderSkill.get();
			if (!skill.getBroaderSkill().contains(broaderSkillUri)) {
				skill.getBroaderSkill().add(broaderSkillUri);
				ResourceLink rLink = new ResourceLink();
				rLink.setPreferredLabel(broaderSkill.getPreferredLabel());
				rLink.setUri(broaderSkill.getUri());
				rLink.setConceptType(broaderSkill.getConceptType());
				skill.getBroaderSkillLink().add(rLink);
				skillRepository.save(skill);
			}
			if (!broaderSkill.getNarrowerSkill().contains(skillUri)) {
				broaderSkill.getNarrowerSkill().add(skillUri);
				ResourceLink rLink = new ResourceLink();
				rLink.setPreferredLabel(skill.getPreferredLabel());
				rLink.setUri(skill.getUri());
				rLink.setConceptType(skill.getConceptType());
				broaderSkill.getNarrowerSkillLink().add(rLink);
				skillRepository.save(broaderSkill);
			}
			logger.info("importSkillParentChildRelations:{}/{}", skillUri, broaderSkillUri);
		}
		// add skill group object and then create 2 different API that return object as per type once for competenceDetails one for group details.
		// if present skill hierachy in this file -> conceptUri value ()
// Level 0 URI	Level 0 preferred term	Level 1 URI	Level 1 preferred term	Level 2 URI	Level 2 preferred term	Level 3 URI	Level 3 preferred term	Description	Scope note	Level 0 code	Level 1 code	Level 2 code	Level 3 code

		
		csvParser.close();
		reader.close();

	}

}
