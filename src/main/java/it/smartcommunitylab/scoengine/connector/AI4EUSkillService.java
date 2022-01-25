package it.smartcommunitylab.scoengine.connector;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.scoengine.common.Const;
import it.smartcommunitylab.scoengine.common.HttpUtils;
import it.smartcommunitylab.scoengine.model.ai4eu.APIResponse;
import it.smartcommunitylab.scoengine.model.ai4eu.Resource;
import it.smartcommunitylab.scoengine.model.esco.Skill;
import it.smartcommunitylab.scoengine.model.esco.SkillSearchStub;
import it.smartcommunitylab.scoengine.repository.SkillRepository;

@Service
public class AI4EUSkillService {
	private static Log logger = LogFactory.getLog(AI4EUSkillService.class);
	@Value("${ai4eu.competences.api}")
	private String apiUrl;
	@Autowired
	private HttpUtils httpUtils;
	@Autowired
	private SkillRepository skillRepository;

	public List<SkillSearchStub> searchSemanticSkill(String text, Boolean isTransversal, int size) throws Exception {
		List<SkillSearchStub> result = new ArrayList<>();
		List<String> uris = new ArrayList<>();
		uris = this.fetchESCOSkill(text, size, Const.AI4EU_RUNTYPE);
		List<Skill> searchList = new ArrayList<>();
		if (isTransversal) {
			searchList = skillRepository.findSkill(uris, true);
		} else {
			searchList = skillRepository.findSkill(uris, false);
		}
		for (Skill sk : searchList) {
			SkillSearchStub sst = new SkillSearchStub();
			sst.setConceptType(sk.getConceptType());
			sst.setUri(sk.getUri());
			sst.setPreferredLabel(sk.getPreferredLabel());
			sst.setReuseLevel(sk.getReuseLevel());
			sst.setHierarchy(sk.getBroaderSkillLink());
			result.add(sst);
		}
		return result;
	}

	public List<String> fetchESCOSkill(String text, int limit, int runType) throws Exception {
		List<String> ids = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("text", text);
			params.put("numResults", String.valueOf(limit));
			params.put("run_type", String.valueOf(runType));
			String response = httpUtils.postJSON(new URI(apiUrl), params).get();
			if (response != null) {
				JsonFactory jsonFactory = new JsonFactory();
				jsonFactory.setCodec(objectMapper);
				JsonParser jp = jsonFactory.createParser(response);
				APIResponse mappedResponse = jp.readValueAs(APIResponse.class);
				for (Resource resource : mappedResponse.getValues_tfidf()) {
					ids.add(resource.getKey());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);

		}
		return ids;

	}
}
