package it.smartcommunitylab.scoengine.repository;

import java.util.List;

import it.smartcommunitylab.scoengine.model.esco.Skill;

public interface SkillRepositoryCustom {
	List<Skill> findSkill(List<String> ids, Boolean isTransversal);
}
