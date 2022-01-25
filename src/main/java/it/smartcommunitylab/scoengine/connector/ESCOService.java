package it.smartcommunitylab.scoengine.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.scoengine.common.Const;
import it.smartcommunitylab.scoengine.common.HttpUtils;
import it.smartcommunitylab.scoengine.exception.BadRequestException;
import it.smartcommunitylab.scoengine.lucene.LuceneManager;
import it.smartcommunitylab.scoengine.model.TextDoc;
import it.smartcommunitylab.scoengine.model.esco.EscoResponse;
import it.smartcommunitylab.scoengine.model.esco.Skill;
import it.smartcommunitylab.scoengine.model.esco.SkillGroup;
import it.smartcommunitylab.scoengine.model.esco.SkillSearchStub;
import it.smartcommunitylab.scoengine.repository.SkillGroupRepository;
import it.smartcommunitylab.scoengine.repository.SkillRepository;

@Service
public class ESCOService {
	private static Log logger = LogFactory.getLog(ESCOService.class);
	@Value("${esco.api}")
	private String apiUrl;
	@Autowired
	private HttpUtils httpUtils;
	@Autowired
	LuceneManager luceneManager;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private SkillGroupRepository skillGroupRepository;

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
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);

		}
		return response;
	}

	public List<SkillSearchStub> searchSkill(String text, Boolean isTransversal, int size) throws Exception {
		List<SkillSearchStub> result = new ArrayList<>();
		List<String> uris = new ArrayList<>();
		uris = luceneManager.searchIdsByFields(text, Const.ESCO_CONCEPT_SKILL, isTransversal, size);
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
			sst.setHiearchy(sk.getBroaderSkillLink());
			result.add(sst);
		}
		return result;
	}

	public List<TextDoc> getField(String fieldTitle, String fieldValue, int maxSize) throws Exception {
		List<TextDoc> skills = new ArrayList<TextDoc>();
		skills = luceneManager.searchBySingleField(fieldTitle, fieldValue, maxSize);
		return skills;
	}

	public List<TextDoc> getByUri(String url, int maxSize) throws Exception {
		List<TextDoc> skills = new ArrayList<TextDoc>();
		skills = luceneManager.searchByURI(url, maxSize);
		return skills;
	}

	public Skill getByUri(String uri) throws Exception {
		Optional<Skill> optional = skillRepository.findById(uri);
		if (optional.isPresent()) {
			Skill skill = optional.get();
			return skill;
		} else {
			throw new BadRequestException("No skill with uri present");
		}
	}

	public SkillGroup getSkillGroupByUri(String uri) throws Exception {
		Optional<SkillGroup> optional = skillGroupRepository.findById(uri);
		if (optional.isPresent()) {
			SkillGroup skillGroup = optional.get();
			return skillGroup;
		} else {
			throw new BadRequestException("No skillGroup with uri present");
		}
	}

}
