package it.smartcommunitylab.scoengine.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import it.smartcommunitylab.scoengine.model.esco.Skill;

public class SkillRepositoryCustomImpl implements SkillRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Skill> findSkill(boolean skillGroup, List<String> isEssentialForOccupation,
			List<String> isOptionalForOccupation, Pageable pageable) {
//		Criteria criteria = skillGroup ? Criteria.where("conceptType").is(Const.CONCEPT_SKILL_GROUP) :
//			Criteria.where("conceptType").is(Const.CONCEPT_SKILL);
//		if((isEssentialForOccupation != null) && (isEssentialForOccupation.size() > 0)) {
//			criteria = criteria.and("isEssentialForOccupation").in(isEssentialForOccupation);
//		}
//		if((isOptionalForOccupation != null) && (isOptionalForOccupation.size() > 0)) {
//			criteria = criteria.and("isOptionalForOccupation").in(isOptionalForOccupation);
//		}
		Query query = new Query();//criteria
//		query.with(new Sort(Direction.ASC, "preferredLabel.it"));
//		if(pageable != null) {
//			query.with(pageable);
//		}
		return mongoTemplate.find(query, Skill.class);
	}

}
