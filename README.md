# spring-myphotos
 
(Work in progress)

This is non-SPA/non-PWA version of "myphotos" project. This project uses Thymeleaf as a template engine for Spring MVC.

## Relationship to my other project on GitHub

[react-myphotos(frontend)](https://github.com/araobp/react-myphotos) --- REST API --- spring-myphotos(backend)

## Set up

### Connection to Postgres SQL

Add "application.properties" file to the project root directory with the following properties:

```
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql:<... URL of your Postgres SQL database ...>
spring.datasource.username=<username>
spring.datasource.password=<password>
```

Or follow the following instructions if this app is deployed to Heroku: https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku

### HTTP Basic auth

Add the following environment variables:
```
ARAOBP_MYPHOTOS_USERNAME_DEFAULT
ARAOBP_MYPHOTOS_PASSWORD_DEFAULT
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

## psql output

```
DATABASE=> \d record
                                 Table "public.record"
  Column   |     Type      | Collation | Nullable |              Default               
-----------+---------------+-----------+----------+------------------------------------
 id        | integer       |           | not null | nextval('record_id_seq'::regclass)
 datetime  | text          |           |          | 
 place     | text          |           |          | 
 memo      | text          |           |          | 
 latitude  | numeric(12,8) |           |          | 
 longitude | numeric(12,8) |           |          | 
Indexes:
    "record_pkey" PRIMARY KEY, btree (id)
Referenced by:
    TABLE "photo" CONSTRAINT "photo_record_fkey" FOREIGN KEY (record_id) REFERENCES record(id) ON DELETE CASCADE

DATABASE=> \d photo
                 Table "public.photo"
  Column   |  Type   | Collation | Nullable | Default 
-----------+---------+-----------+----------+---------
 record_id | integer |           | not null | 
 image     | bytea   |           |          | 
 thumbnail | bytea   |           |          | 
Indexes:
    "photo_pkey" PRIMARY KEY, btree (record_id)
Foreign-key constraints:
    "photo_record_fkey" FOREIGN KEY (record_id) REFERENCES record(id) ON DELETE CASCADE

DATABASE=> \d gps_log
                                 Table "public.gps_log"
  Column   |     Type      | Collation | Nullable |               Default               
-----------+---------------+-----------+----------+-------------------------------------
 id        | integer       |           | not null | nextval('gps_log_id_seq'::regclass)
 datetime  | text          |           |          | 
 latitude  | numeric(12,8) |           |          | 
 longitude | numeric(12,8) |           |          | 
Indexes:
    "gps_log_pkey" PRIMARY KEY, btree (id)

```

## Tips

Table initialization
```
=> DELETE FROM record;
=> SELECT setval('record_id_seq', 1, false);
```
