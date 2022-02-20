# spring-myphotos
 
(Work in progress)

This is non-SPA/non-PWA version of "myphotos" project. This project uses Thymeleaf as a template engine for Spring MVC.

## Relationship to my other project on GitHub

[react-myphotos(frontend)](https://github.com/araobp/react-myphotos) --- REST API --- spring-myphotos

## Set up

Add "application.properties" file with the following properties:

```
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql:<... URL of your Postgres SQL database ...>
spring.datasource.username=<username>
spring.datasource.password=<password>
```

## REST API

```
*** CREATE A RECORD ***
POST /record
{
    "place": "string",
    "memo": "string",
    "latitude": "number",
    "longitude": "number"
}

Its response
{
    "id": "integer"
}

*** UPDATE A RECORD ***
PUT /record/:id
{
    "place": "string",
    "memo": "string"
}

*** GET A LIST OF RECORDS ***
GET /record?limit={limit}&offset={offset}
[{"id": "number", 
  "record":
    {
        "datetime": "string",
        "place": "string",
        "memo": "string",
        "latitude": "number",
        "longitude": "number"
    }
}]

*** GET A RECORD ***
GET /record/:id
{
    "datetime: "string",
    "place": "string",
    "memo": "string",
    "latitude": "number",
    "longitude": "number"
}

*** PUT AN IMAGE ***
PUT /photo/:id
Binary data

*** GET A THUMBNAIL ***
GET /photo/:id/thumbnail
Binary data

*** GET AN IMAGE ***
GET /photo/:id/image
Binary data

*** DELETE A RECORD ***
DELETE /record/:id

Its associated photo is also deleted.

```

## Tips

Table initialization
```
=> DELETE FROM record;
=> SELECT setval('record_id_seq', 1, false);
```
