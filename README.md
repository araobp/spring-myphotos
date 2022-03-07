# spring-myphotos
 
REST API server for [react-myphotos](https://github.com/araobp/react-myphotos)

## REST API specification

[REST API specification in OpenAPI YAML format](./doc/restapi.yaml)


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

DATABASE=> \d gps_log;
                                 Table "public.gps_log"
  Column   |     Type      | Collation | Nullable |               Default               
-----------+---------------+-----------+----------+-------------------------------------
 id        | integer       |           | not null | nextval('gps_log_id_seq'::regclass)
 datetime  | text          |           |          | 
 latitude  | numeric(12,8) |           |          | 
 longitude | numeric(12,8) |           |          | 
 session   | integer       |           |          | 
Indexes:
    "gps_log_pkey" PRIMARY KEY, btree (id)
```

## Tips

### Postgres SQL table initialization
```
=> DELETE FROM record;
=> SELECT setval('record_id_seq', 1, false);
```

### Resizing image preserving EXIF orientation by Thumnailator

Use an instance of ByteArrayInputStream as an input of Thumbnails.of():

```
InputStream inputStream = new ByteArrayInputStream(image);
Thumbnails.of(inputStream).size(THUMBNAIL_TARGET_WIDTH, targetHeight).outputFormat("JPEG")
    .outputQuality(1).toOutputStream(outputStream);
```

