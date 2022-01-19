package it.smartcommunitylab.scoengine.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.scoengine.common.Const;
import it.smartcommunitylab.scoengine.common.HttpUtils;
import it.smartcommunitylab.scoengine.exception.BadRequestException;
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

	public List<TextDoc> searchSkill(String text, int size) throws ParseException, IOException {
		List<TextDoc> searchList = new ArrayList<>();
		searchList = luceneManager.searchByFields(text, Const.CONCEPT_SKILL, null, size);
		return searchList;
	}

}
