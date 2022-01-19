package it.smartcommunitylab.scoengine.csv;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

@Component
public class CsvManager {
	private static final transient Logger logger = LoggerFactory.getLogger(CsvManager.class);

	@Autowired
	private LuceneManager luceneManager;

	public void indexSkills(String csvFilePath) throws IOException {
		List<Document> docs = new ArrayList<Document>();
		Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
		CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("conceptType", "conceptUri", "skillType", "reuseLevel",
				"preferredLabel", "altLabels", "hiddenLabels", "status", "modifiedDate", "scopeNote", "definition", "inScheme", "description").withSkipHeaderRecord();
	
		CSVParser csvParser = new CSVParser(reader, csvFormat);
		for (CSVRecord record : csvParser) {
			Document doc = new Document();
			doc.add(new Field("uri", record.get("conceptUri"), TextField.TYPE_STORED));
			doc.add(new Field("conceptType", record.get("conceptType"), TextField.TYPE_STORED));
			doc.add(new Field("reuseLevel", record.get("reuseLevel"), TextField.TYPE_STORED));
			doc.add(new Field("preferredLabel", record.get("preferredLabel"), TextField.TYPE_STORED));
			doc.add(new Field("altLabels", record.get("altLabels"), TextField.TYPE_STORED));
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

}
