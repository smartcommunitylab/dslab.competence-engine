package it.smartcommunitylab.scoengine.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.scoengine.connector.ESCOService;
import it.smartcommunitylab.scoengine.model.TextDoc;
import it.smartcommunitylab.scoengine.model.esco.EscoResponse;
import it.smartcommunitylab.scoengine.model.esco.Skill;
import it.smartcommunitylab.scoengine.model.esco.SkillGroup;

@RestController
public class ESCOController implements SCOController {
	private static Log logger = LogFactory.getLog(ESCOController.class);
	@Autowired
	private ESCOService escoService;

	@GetMapping("/api/search/skill")
	public List<TextDoc> searchSkill(@RequestParam String text, @RequestParam(required = false) String language,
			@RequestParam(required = false) Boolean isTransversal, @RequestParam int size) throws Exception {
		text = StringUtils.strip(text);
		List<TextDoc> result = escoService.searchSkill(text, (isTransversal!=null) ? isTransversal : false, size);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("/api/search/skill:%s" + " size:%s", text, result.size()));
		}
		return result;
	}

	@GetMapping("/api/skill/uri")
	public Skill getSkillByUri(@RequestParam String uri) throws Exception {
		return escoService.getByUri(uri);
	}

	@GetMapping("/api/skillGroup/uri")
	public SkillGroup getSkillGroupByUri(@RequestParam String uri) throws Exception {
		return escoService.getSkillGroupByUri(uri);
	}

	@GetMapping("/api/field/value")
	public List<TextDoc> getRowsByFieldValue(@RequestParam String fieldTitle, @RequestParam String fieldValue,
			@RequestParam String limit) throws Exception {
		List<TextDoc> result = escoService.getField(fieldTitle, fieldValue, Integer.valueOf(limit));
		if (logger.isInfoEnabled()) {
			logger.info(String.format("/api/search/uri <%s,%s>", fieldTitle, fieldValue));
		}
		return result;
	}

	@GetMapping("/api/search/occupation")
	public EscoResponse searchOccupation(@RequestParam String text, Pageable pageRequest) throws Exception {
		EscoResponse result = escoService.searchOccupation(text, pageRequest);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("/api/search/occupation:%s", text));
		}
		return result;
	}
}
