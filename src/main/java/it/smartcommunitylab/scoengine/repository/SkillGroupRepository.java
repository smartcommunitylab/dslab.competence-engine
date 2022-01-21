package it.smartcommunitylab.scoengine.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.smartcommunitylab.scoengine.model.esco.SkillGroup;

@Repository
public interface SkillGroupRepository extends MongoRepository<SkillGroup, String> {

}
