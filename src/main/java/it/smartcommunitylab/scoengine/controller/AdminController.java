package it.smartcommunitylab.scoengine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.scoengine.csv.CsvManager;

@RestController
public class AdminController implements SCOController {
	private static final transient Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	CsvManager csvManager;

	@GetMapping(value = "/admin/index/all")
	public void indexAll(@RequestParam String path) throws Exception {
		csvManager.indexSkills(path + "/skills_it.csv");
		logger.info("indexAll:{}", path);
	}

	@GetMapping(value = "/admin/import/all")
	public void importAll(@RequestParam String path) throws Exception {
		csvManager.importSkills(path + "/skills_it.csv", "it");
		csvManager.importSkillGroups(path + "/skillGroups_it.csv", "it");
		csvManager.importSkillEssentialRelations(path + "/skillSkillRelations.csv");
		csvManager.importSkillBroaderRelation(path + "/broaderRelationsSkillPillar.csv");
		logger.info("importAll:{}", path);
	}

}
