package it.smartcommunitylab.scoengine.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.scoengine.connector.AI4EUSkillService;
import it.smartcommunitylab.scoengine.model.esco.SkillSearchStub;

@RestController
public class AI4EUSkillController implements SCOController {
	private static Log logger = LogFactory.getLog(AI4EUSkillController.class);
	@Autowired
	AI4EUSkillService ai4euSkillService;

	@GetMapping("/api/search/semantic/skill")
	public List<SkillSearchStub> searchSkill(@RequestParam String text, @RequestParam(required = false) String language,
			@RequestParam(required = false) Boolean isTransversal, @RequestParam int size) throws Exception {
		text = StringUtils.strip(text);
		List<SkillSearchStub> result = ai4euSkillService.searchSemanticSkill(text,
				(isTransversal != null) ? isTransversal : false, size);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("/api/search/semantic/skill:%s" + " size:%s" + "isTransversal:%s", text,
					isTransversal, result.size()));
		}
		return result;
	}

}
