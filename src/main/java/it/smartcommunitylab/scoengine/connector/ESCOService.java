package it.smartcommunitylab.scoengine.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.scoengine.common.Const;
import it.smartcommunitylab.scoengine.common.HttpUtils;
import it.smartcommunitylab.scoengine.lucene.LuceneManager;
import it.smartcommunitylab.scoengine.model.TextDoc;
import it.smartcommunitylab.scoengine.model.esco.EscoResponse;

@Service
public class ESCOService {
	private static Log logger = LogFactory.getLog(ESCOService.class);
	@Value("${esco.api}")
	private String apiUrl;
	@Autowired
	private HttpUtils httpUtils;
	@Autowired
	LuceneManager luceneManager;
	
	public EscoResponse searchOccupation(String text, Pageable pageRequest) throws Exception {
		EscoResponse searchResult = fetchESCOOccupation(text, pageRequest.getPageSize(), pageRequest.getPageNumber());
		return searchResult;
	}

	private EscoResponse fetchESCOOccupation(String text, int limit, int offset) throws Exception {
		EscoResponse response = null;
		try {
			response = httpUtils.sendGET(apiUrl, text, limit, offset);
			if (response != null) {
				
			}
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
			
		}
		return response;
//		throw new BadRequestException("Not null id");
	}

	public List<TextDoc> searchSkill(String text, int size) throws Exception {
		List<TextDoc> searchList = new ArrayList<>();
		searchList = luceneManager.searchByFields(text, Const.ESCO_CONCEPT_SKILL, null, size);
		return searchList;
	}

	public List<TextDoc> getField(String fieldTitle, String fieldValue, int maxSize) throws Exception {
		List<TextDoc> skills = new ArrayList<TextDoc>();
		skills = luceneManager.searchBySingleField(fieldTitle, fieldValue, maxSize);
		return skills;
	}

	public List<TextDoc> getByUri(String uri, int maxResult) throws Exception {
		List<TextDoc> results = new ArrayList<TextDoc>();
		results = luceneManager.searchByURI(uri, maxResult);
		return results;
	}

}
