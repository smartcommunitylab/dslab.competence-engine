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

## Search Query

The API execpt 'text' parameter as input search key and 'size' parameter determine the number of skills to search.

@GetMapping("/api/search/skill")
	@RequestParam String text,
	@RequestParam String language,
	@RequestParam String size)

[
    {
        "fields": {
            "hiearchy": "",
            "description": "",
            "label": "",
            "type": "",
            "uri": ""
        },
        "score": 0
    }..
]

Sample Url: http://localhost:4500/sco/api/search/skill?text=javascript&size=10

## Swagger
http://localhost:4500/sco/swagger-ui.html#/esco-controller
