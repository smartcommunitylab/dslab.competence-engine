package it.smartcommunitylab.scoengine.lucene;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import eu.fbk.dh.tint.runner.TintPipeline;
import it.smartcommunitylab.scoengine.common.Const;
import it.smartcommunitylab.scoengine.model.TextDoc;

@Component
public class LuceneManager {
	private static final transient Logger logger = LoggerFactory.getLogger(LuceneManager.class);

	@Autowired
	@Value("${lucene.index.path}")
	private String indexPath;
	private Analyzer analyzer;
	private Directory directory;
	private IndexWriterConfig config;
	private DirectoryReader ireader;
	private IndexSearcher isearcher;
	private IndexWriter iwriter;
	private TintPipeline pipeline;

	@PostConstruct
	public void init() throws IOException {
		analyzer = new ItalianAnalyzer();
		Path path = Paths.get(indexPath);
		if (!Files.exists(path)) {
			Files.createDirectory(path);
		}
		directory = FSDirectory.open(path);
		config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		iwriter = new IndexWriter(directory, config);
		iwriter.commit();
		ireader = DirectoryReader.open(directory);
		isearcher = new IndexSearcher(ireader);
		pipeline = new TintPipeline();
		pipeline.loadDefaultProperties();
		pipeline.setProperty("annotators", "ita_toksent, pos, ita_morpho, ita_lemma");
		pipeline.load();
	}

	@PreDestroy
	public void close() throws IOException {
		iwriter.close();
		ireader.close();
		directory.close();
	}

	public String normalizeText(String... strings) {
		StringBuffer sb = new StringBuffer();
		for (String text : strings) {
			Annotation stanfordAnnotation = pipeline.runRaw(text.toLowerCase());
			for (CoreMap sentence : stanfordAnnotation.get(SentencesAnnotation.class)) {
				for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
					if (token.lemma().equalsIgnoreCase("[PUNCT]")) {
						continue;
					}
					sb.append(token.lemma());
					sb.append(" ");
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public void indexDocuments(List<Document> docs) throws IOException {
		iwriter.deleteAll();
		iwriter.commit();
		for (Document doc : docs) {
			iwriter.addDocument(doc);
		}
		iwriter.commit();
		ireader = DirectoryReader.open(directory);
		isearcher = new IndexSearcher(ireader);
		logger.debug("indexDocuments:{}", docs.size());
	}

	public List<TextDoc> searchByFields(String text, String conceptType, Boolean isTransversal, int maxResult)
			throws ParseException, IOException {
		BooleanQuery booleanQuery = null;

		String[] fields = new String[] { "preferredLabelNormalized", "altLabelsNormalized", "descriptionNormalized" };
		Map<String, Float> boosts = new HashMap<String, Float>();
		boosts.put("descriptionNormalized", Float.valueOf(1.05f));
		boosts.put("preferredLabelNormalized", Float.valueOf(1.05f));
		boosts.put("altLabelsNormalized", Float.valueOf(0.90f));
		QueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
		Query fieldQuery = parser.parse(QueryParser.escape(normalizeText(text)));

//		SimpleQueryParser simpleParser = new SimpleQueryParser(analyzer, "conceptType");
//		Query typeQuery = simpleParser.parse(conceptType);

		if (isTransversal) {
			QueryParser parserReuseLevel = new QueryParser("reuseLevel", analyzer);
			Query reuseLevelQuery = parserReuseLevel.parse("transversal");
			booleanQuery = new BooleanQuery.Builder().add(fieldQuery, BooleanClause.Occur.MUST)
//					.add(typeQuery, BooleanClause.Occur.MUST)
					.add(reuseLevelQuery, BooleanClause.Occur.MUST).build();
		} else {
			booleanQuery = new BooleanQuery.Builder().add(fieldQuery, BooleanClause.Occur.MUST)
//					.add(typeQuery, BooleanClause.Occur.MUST)
					.build();
		}

		ScoreDoc[] hits = isearcher.search(booleanQuery, maxResult).scoreDocs;
		List<TextDoc> result = new ArrayList<TextDoc>();
		for (ScoreDoc scoreDoc : hits) {
			Document doc = isearcher.doc(scoreDoc.doc);
			TextDoc textDoc = new TextDoc();
			textDoc.setScore(scoreDoc.score);
			textDoc.getFields().put("label", doc.get("preferredLabel"));
			textDoc.getFields().put("type", doc.get("conceptType"));
			textDoc.getFields().put("uri", doc.get("uri"));
			textDoc.getFields().put("description", doc.get("description"));
			textDoc.getFields().put("reuseLevel", doc.get("reuseLevel"));
			result.add(textDoc);
		}
		logger.debug("searchByFields:{}/{}/{}/{}", result.size(), conceptType, isTransversal, text);
		return result;
	}

	public List<String> searchIdsByFields(String text, Boolean isTransversal, int maxResult)
			throws ParseException, IOException {
		List<String> result = new ArrayList<String>();
		if (isTransversal == null) {
			result = fetchAll(text, maxResult);
			logger.debug("searchByFields:{}/{}/{}", result.size(), isTransversal, text);

		} else if (isTransversal) {
			result = fetchTransversalNonLanguageOnly(text, maxResult);
			logger.debug("searchByFields:{}/{}/{}", result.size(), isTransversal, text);

		} else if (!isTransversal) {
			result = fetchSpecificWithTransversalLanguage(text, maxResult);
			logger.debug("searchByFields:{}/{}/{}", result.size(), isTransversal, text);
		}
		return result;
	}

	private List<String> fetchAll(String text, int maxResult) throws ParseException, IOException {
		String[] fields = new String[] { "preferredLabelNormalized", "altLabelsNormalized", "descriptionNormalized" };
		Map<String, Float> boosts = new HashMap<String, Float>();
		boosts.put("descriptionNormalized", Float.valueOf(1.05f));
		boosts.put("preferredLabelNormalized", Float.valueOf(1.05f));
		boosts.put("altLabelsNormalized", Float.valueOf(0.90f));
		QueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
		Query fieldQuery = parser.parse(QueryParser.escape(normalizeText(text)));
		BooleanQuery booleanQuery = new BooleanQuery.Builder()
				.add(fieldQuery, BooleanClause.Occur.MUST)
				.build();
		ScoreDoc[] hits = isearcher.search(booleanQuery, maxResult).scoreDocs;
		List<String> result = new ArrayList<String>();
		for (ScoreDoc scoreDoc : hits) {
			Document doc = isearcher.doc(scoreDoc.doc);
			result.add(doc.get("uri"));
		}
		return result;
	}
	
	private List<String> fetchTransversalNonLanguageOnly(String text, int maxResult)
			throws ParseException, IOException {
		String[] fields = new String[] { "preferredLabelNormalized", "altLabelsNormalized", "descriptionNormalized" };
		Map<String, Float> boosts = new HashMap<String, Float>();
		boosts.put("descriptionNormalized", Float.valueOf(1.05f));
		boosts.put("preferredLabelNormalized", Float.valueOf(1.05f));
		boosts.put("altLabelsNormalized", Float.valueOf(0.90f));
		QueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
		Query fieldQuery = parser.parse(QueryParser.escape(normalizeText(text)));
		QueryParser parserReuseLevel = new QueryParser("reuseLevel", analyzer);
		Query reuseLevelQuery = parserReuseLevel.parse(Const.ESCO_TRANSVERSAL_SKILL);
		QueryParser parserInScheme = new QueryParser("inScheme", analyzer);
		Query inSchemeQuery = parserInScheme.parse("\"http://data.europa.eu/esco/concept-scheme/skill-language-groups\"");
		BooleanQuery booleanQuery = new BooleanQuery.Builder()
				.add(inSchemeQuery, BooleanClause.Occur.MUST_NOT)
				.add(reuseLevelQuery, BooleanClause.Occur.MUST)
				.add(fieldQuery, BooleanClause.Occur.MUST)
				.build();
		ScoreDoc[] hits = isearcher.search(booleanQuery, maxResult).scoreDocs;
		List<String> result = new ArrayList<String>();
		for (ScoreDoc scoreDoc : hits) {
			Document doc = isearcher.doc(scoreDoc.doc);
			result.add(doc.get("uri"));
		}
		return result;
	}

	private List<String> fetchSpecificWithTransversalLanguage(String text, int maxResult)
			throws ParseException, IOException {
		List<String> result = new ArrayList<String>();
		String[] fields = new String[] { "preferredLabelNormalized", "altLabelsNormalized", "descriptionNormalized" };
		Map<String, Float> boosts = new HashMap<String, Float>();
		boosts.put("descriptionNormalized", Float.valueOf(1.05f));
		boosts.put("preferredLabelNormalized", Float.valueOf(1.05f));
		boosts.put("altLabelsNormalized", Float.valueOf(0.90f));
		QueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
		Query fieldQuery = parser.parse(QueryParser.escape(normalizeText(text)));
		QueryParser parserReuseLevel = new QueryParser("reuseLevel", analyzer);
		Query reuseLevelQuery = parserReuseLevel.parse(Const.ESCO_TRANSVERSAL_SKILL);
		QueryParser parserInScheme = new QueryParser("inScheme", analyzer);
		Query inSchemeQuery = parserInScheme.parse("\"http://data.europa.eu/esco/concept-scheme/skill-language-groups\"");
		BooleanQuery booleanQueryTransversalLanguage = new BooleanQuery.Builder()
				.add(inSchemeQuery, BooleanClause.Occur.MUST)
				.add(reuseLevelQuery, BooleanClause.Occur.MUST)
				.add(fieldQuery, BooleanClause.Occur.MUST)
				.build();
		ScoreDoc[] hitsTransveralLanguage= isearcher.search(booleanQueryTransversalLanguage, maxResult).scoreDocs;
		List<String> resultTransversalLanguage = new ArrayList<String>();
		for (ScoreDoc scoreDoc : hitsTransveralLanguage){
			Document doc = isearcher.doc(scoreDoc.doc);
			resultTransversalLanguage.add(doc.get("uri"));
		}
		List<String> resultSpecific = new ArrayList<String>();
		BooleanQuery booleanQuerySpecific = new BooleanQuery.Builder()
				.add(fieldQuery, BooleanClause.Occur.MUST)
				.add(reuseLevelQuery, BooleanClause.Occur.MUST_NOT).build();
		ScoreDoc[] hitsSpecific = isearcher.search(booleanQuerySpecific, maxResult).scoreDocs;
		for (ScoreDoc scoreDoc : hitsSpecific) {
			Document doc = isearcher.doc(scoreDoc.doc);
			resultSpecific.add(doc.get("uri"));
		}
		result.addAll(resultTransversalLanguage);
		result.addAll(resultSpecific);
		return result.subList(0, maxResult - 1);
	}
	
	public List<TextDoc> searchBySingleField(String fieldTitle, String fieldValue, int maxResult)
			throws IOException, ParseException {
		SimpleQueryParser simpleParser = new SimpleQueryParser(analyzer, fieldTitle);
		Query typeQuery = simpleParser.parse(fieldValue);
		BooleanQuery booleanQuery = new BooleanQuery.Builder().add(typeQuery, BooleanClause.Occur.SHOULD).build();
		ScoreDoc[] hits = isearcher.search(booleanQuery, maxResult).scoreDocs;
		List<TextDoc> result = new ArrayList<TextDoc>();
		for (ScoreDoc scoreDoc : hits) {
			Document doc = isearcher.doc(scoreDoc.doc);
			TextDoc textDoc = new TextDoc();
			textDoc.getFields().put("preferredLabel", doc.get("preferredLabel"));
			textDoc.getFields().put("altLabels", doc.get("altLabels"));
			textDoc.getFields().put("conceptType", doc.get("conceptType"));
			textDoc.getFields().put("uri", doc.get("uri"));
			result.add(textDoc);
		}
		logger.debug("searchByFields:{}/{}/{}/{}", result.size(), fieldTitle, fieldValue);
		return result;
	}

	public List<TextDoc> searchByURI(String url, int maxResult) throws IOException, ParseException {
		QueryParser parserUri = new QueryParser("uri", analyzer);
		Query urlQuery = parserUri.parse(url);
		BooleanQuery bq = new BooleanQuery.Builder().add(urlQuery, BooleanClause.Occur.MUST).build();

		ScoreDoc[] hits = isearcher.search(bq, maxResult).scoreDocs;
		List<TextDoc> result = new ArrayList<TextDoc>();
		for (ScoreDoc scoreDoc : hits) {
			Document doc = isearcher.doc(scoreDoc.doc);
			TextDoc textDoc = new TextDoc();
			textDoc.getFields().put("preferredLabel", doc.get("preferredLabel"));
			textDoc.getFields().put("altLabels", doc.get("altLabels"));
			textDoc.getFields().put("conceptType", doc.get("conceptType"));
			textDoc.getFields().put("uri", doc.get("uri"));
			result.add(textDoc);
		}
		logger.debug("searchByUri:{}/{}", url, result.size());
		return result;
	}
}
