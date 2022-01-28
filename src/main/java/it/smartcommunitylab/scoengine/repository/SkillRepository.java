package it.smartcommunitylab.scoengine.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import it.smartcommunitylab.scoengine.model.esco.Skill;

@Repository
public interface SkillRepository extends MongoRepository<Skill, String>, SkillRepositoryCustom {

	@Query(value = "{$or:[{isEssentialForOccupation:{$in:?0}},{isOptionalForOccupation:{$in:?0}}]}")
	List<Skill> findByOccupation(String occupationUri);

	@Query(value = "{uri:{$in:?0}}")
	List<Skill> findByIds(List<String> ids);

	@Query(value = "{uri:{$in:?0}}, 'reuseLevel' : ?1", fields = "{ 'uri':1, 'conceptType':1,'reuseLevel':1, 'description':1, 'preferredLabel':1, 'broaderSkillLink':1, 'isTransversal':1}")
	List<Skill> findByIdsByReuseLevel(List<String> ids, String reuseLevel);

}
