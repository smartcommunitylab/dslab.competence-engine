# dslab.competence-engine
SCO Engine is generic engine that run on top of different core engines such as ESCO, ISOC, ISOFL 
It manages the datasets for different cores and expore APIs that allows for manipulation of datsset, search indexing
to query and navigate skill, competence, occupation ontology data.

# Requirement
	Java sdk 11.
	Mongo db.

# ESCO Core
Engine for query and navigate ESCO ontology data

## Search Skill
@GetMapping("/api/search/skill")
@RequestParam String text,
@RequestParam(required = false) String language,
@RequestParam(required = false) Boolean isTransversal,
@RequestParam int size

## Get Skill
	@GetMapping("/api/skill/uri")
	@RequestParam String uri

## Get Skill Group
	@GetMapping("/api/skillGroup/uri")
	@RequestParam String uri

## Swagger
	http://localhost:4500/sco/swagger-ui.html#/esco-controller


## Build
	mvn clean package -Dmaven.test.skip=true

## Run
	java -jar competence-engine-1.0.jar --lucene.index.path=/path/to/esco/csv

Note: The current version is based on ESCO model v1.0.9. One can download the following set of files from
	https://ec.europa.eu/esco/portal/download

Select version v1.0.9, type CSV. The files used for SCO Engine are skills_it.csv, skillGroups_it.csv, skillSkillRelations.csv,
broaderRelationsSkillPillar.csv

## Data Import
In order to import dataset for manipulation and indexing, one need to call the following APIs

	/admin/index/all

The API expects a parameter 'path' which corresponds to location of dataset csv files.

	/admin/import/all

The API expects as paramter 'path' which corresponds to locaton of dataset csv file. It imports data inside 
mongo db collections.
