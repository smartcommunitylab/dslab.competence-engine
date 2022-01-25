package it.smartcommunitylab.scoengine.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import it.smartcommunitylab.scoengine.common.Const;
import it.smartcommunitylab.scoengine.model.esco.Skill;

public class SkillRepositoryCustomImpl implements SkillRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Skill> findSkill(List<String> ids, Boolean isTransversal) {
		Criteria criteria = Criteria.where("uri").in(ids);
		if (isTransversal) {
			criteria = criteria.and("reuseLevel").is(Const.ESCO_TRANSVERSAL_SKILL);
		}
		Query query = new Query(criteria);
		query.fields().include("uri");
		query.fields().include("conceptType");
		query.fields().include("reuseLevel");
		query.fields().include("preferredLabel");
		query.fields().include("broaderSkillLink");
		return mongoTemplate.find(query, Skill.class);
	}

}
