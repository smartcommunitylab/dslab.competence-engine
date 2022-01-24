# dslab.competence-engine
SCO Engine is generic engine that run on top of different core engines such as ESCO, ISOC, ISOFL 
It manages the datasets for different cores and expore APIs that allows for manipulation of datsset, search indexing
to query and navigate skill, competence, occupation ontology data.

# ESCO Core
Engine for query and navigate ESCO ontology data

## Data Import
In order to import dataset for manipulation and indexing, one need to call the following APIs

/admin/index/all

The API expects a parameter 'path' which corresponds to location of dataset csv files

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
