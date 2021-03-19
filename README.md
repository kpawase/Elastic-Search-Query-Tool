# Elastic-Search-Query-Tool 1.0
 A simplified query Apis for Elastic search.
### Prerequisites
```
1. Java 8 or further versions
2. Elastic Search 
3. Data indexed into the Elastic search with appropriate name.
4. Spring tool suite (IDE if requires)
5. Postman (For Api testing purpose)
```
 
## How to setup the project

````
1. Take the pull from the git repo.
2. Add elastic search index name in the application.properties file. (Location: src/main/resources)
3. If STS is available, open the import maven project wizard and import the same project.
4. If using plain MVN, then
    1. Go to the project location and execute following command to build the project
        mvn clean install
    2. To run the project enter the following command.
        mvn spring-boot:run
5. Project will be running on port 8080.
````

## Api Documentation

#### 1.Query API 
````
1. URL : http://localhost:8080/api/query
2. HTTP Method : POST
3. Request Body:
    {
        "queries":[{
            "fieldName":"taxonID", // index field name 
            "fieldValue":"ghr2", // field value for search
            "matchConstrain":"must", // Boolean query value (must, must not, should)
            "queryType":"Term" // Along with term query, Match, wildcard, matchall query is supported
        }
        ],
        "limit":150,
        "queryString":""
    }
4. Response: 
    {
    "statusCode": 200, // Status code
    "message": "Query Executed Successfully", // Success Message
    "data": [ // data returned by elastic search. This data is returned from Taxonomy index.
        {
            "namePublishedIn": null,
            "nomenclaturalStatus": null,
            "nomenclaturalCode": "ICZN",
            "path": "/Users/abc/Downloads/2020-12-01_dwca/Taxon.tsv",
            "@timestamp": "2021-03-05T19:59:16.307Z",
            "taxonID": "GHR2",
            "taxonRank": "SPECIES",
            "specificEpithet": "nemoralis",
            "taxonRemarks": null,
            "@version": "1",
            "genericName": "Argutor",
            "acceptedNameUsageID": "6WM3W",
            "scientificName": "Argutor nemoralis (Graells, 1851)",
            "infraspecificEpithet": null,
            "references": null,
            "nameAccordingTo": null,
            "message": "GHR2\t\t6WM3W\t\tSYNONYM\tSPECIES\tArgutor nemoralis (Graells, 1851)\tArgutor\tnemoralis\t\t\t\tICZN\t\t\t",
            "parentNameUsageID": null,
            "taxonomicStatus": "SYNONYM",
            "host": "Kirans-MacBook-Pro.local",
            "originalNameUsageID": null
        }
    ]
}    
````

#### 2. Submit Raw Json Query

````
1. URL : http://localhost:8080/api/jsonquery
2. HTTP Method: POST
3. Request Body:
{
    "queries":[
    ],
    "limit":0,
    "queryString" : "{ \"query\": { \"bool\": { \"must\": [ { \"match\": { \"taxonRank\": \"SPECIES\" } } ], \"must_not\": [ ], \"should\": [ ] } }, \"from\": 0, \"size\": 1000}"
}

4. This API would return Raw json object returned by elastic search as it is.

````

 
