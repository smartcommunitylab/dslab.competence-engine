package it.smartcommunitylab.scoengine.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import it.smartcommunitylab.scoengine.model.esco.Skill;

public interface SkillRepositoryCustom {
	List<Skill> findSkill(boolean skillGroup, List<String> isEssentialForOccupation, 
			List<String> isOptionalForOccupation, Pageable pageable);
}
