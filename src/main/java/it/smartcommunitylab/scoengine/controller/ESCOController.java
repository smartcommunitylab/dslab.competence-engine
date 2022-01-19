package it.smartcommunitylab.scoengine.controller;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import it.smartcommunitylab.scoengine.connector.ESCOService;
import it.smartcommunitylab.scoengine.model.TextDoc;
import it.smartcommunitylab.scoengine.model.esco.EscoResponse;


@RestController
public class ESCOController implements SCOController {
	private static Log logger = LogFactory.getLog(ESCOController.class);
	@Autowired
	private ESCOService escoService;
	
		
	@GetMapping("/api/search/skill")
	public List<TextDoc> searchSkill(@RequestParam String text,
			@RequestParam(required=false) String language,
			@RequestParam int size) throws Exception {
		text = StringUtils.strip(text);
		List<TextDoc> result = escoService.searchSkill(text, size);
		if(logger.isInfoEnabled()) {
			logger.info(String.format("/api/search/skill:%s" + " size:%s", text, result.size()));
		}
		return result;
	}
	
	@GetMapping("/api/search/occupation")
	public EscoResponse searchOccupation(@RequestParam String text, Pageable pageRequest) throws Exception {
		EscoResponse result = escoService.searchOccupation(text, pageRequest);
		if(logger.isInfoEnabled()) {
			logger.info(String.format("/api/search/occupation:%s", text));
		}
		return result;
	}
}
