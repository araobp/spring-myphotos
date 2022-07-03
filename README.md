# spring-myphotos
 
REST API server for [react-myphotos](https://github.com/araobp/react-myphotos)

## REST API specification

[REST API specification in RAML format](./doc/api.raml)

## Relationship to my other project on GitHub

[react-myphotos(frontend)](https://github.com/araobp/react-myphotos) --- REST API --- spring-myphotos(backend)

## Set up

### Connection to PostgreSQL

Add "application.properties" file to src/main/resources directory with the following properties:

```
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql:<... URL of your PostgreSQL database ...>
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
DATABASE=> set search_path to salesforce;                                                                                                     SET

DATABASE=> \d
                       List of relations
   Schema   |         Name         |   Type   |     Owner      
------------+----------------------+----------+----------------
 salesforce | _hcmeta              | table    | bpkutsjczkjxhz
 salesforce | _hcmeta_id_seq       | sequence | bpkutsjczkjxhz
 salesforce | _sf_event_log        | table    | bpkutsjczkjxhz
 salesforce | _sf_event_log_id_seq | sequence | bpkutsjczkjxhz
 salesforce | _trigger_log         | table    | bpkutsjczkjxhz
 salesforce | _trigger_log_archive | table    | bpkutsjczkjxhz
 salesforce | _trigger_log_id_seq  | sequence | bpkutsjczkjxhz
 salesforce | photo                | table    | bpkutsjczkjxhz
 salesforce | record__c            | table    | bpkutsjczkjxhz
 salesforce | record__c_id_seq     | sequence | bpkutsjczkjxhz
(10 rows)

DATABASE=> \d record__c;
                                              Table "salesforce.record__c"
          Column           |            Type             | Collation | Nullable |                Default                
---------------------------+-----------------------------+-----------+----------+---------------------------------------
 geolocation__latitude__s  | double precision            |           |          | 
 memo__c                   | character varying(255)      |           |          | 
 geolocation__longitude__s | double precision            |           |          | 
 name                      | character varying(80)       |           |          | 
 timestamp__c              | character varying(40)       |           |          | 
 address__c                | character varying(200)      |           |          | 
 isdeleted                 | boolean                     |           |          | 
 systemmodstamp            | timestamp without time zone |           |          | 
 createddate               | timestamp without time zone |           |          | 
 sfid                      | character varying(18)       | ucs_basic |          | 
 id                        | integer                     |           | not null | nextval('record__c_id_seq'::regclass)
 _hc_lastop                | character varying(32)       |           |          | 
 _hc_err                   | text                        |           |          | 
 record_id__c              | double precision            |           |          | 
Indexes:
    "record__c_pkey" PRIMARY KEY, btree (id)
    "hc_idx_record__c_systemmodstamp" btree (systemmodstamp)
    "hcu_idx_record__c_sfid" UNIQUE, btree (sfid)
    "hcu_idx_record__c_timestamp__c" UNIQUE, btree (timestamp__c)
Referenced by:
    TABLE "photo" CONSTRAINT "photo_record_fkey" FOREIGN KEY (record_id) REFERENCES record__c(id) ON DELETE CASCADE
Triggers:
    hc_record__c_logtrigger AFTER INSERT OR DELETE OR UPDATE ON record__c FOR EACH ROW WHEN (public.get_xmlbinary()::text = 'base64'::text) EXECUTE FUNCTION hc_record__c_logger()
    hc_record__c_status_trigger BEFORE INSERT OR UPDATE ON record__c FOR EACH ROW EXECUTE FUNCTION hc_record__c_status()

DATABASE=> \d photo;
                  Table "salesforce.photo"
     Column      |  Type   | Collation | Nullable | Default 
-----------------+---------+-----------+----------+---------
 image           | bytea   |           |          | 
 thumbnail       | bytea   |           |          | 
 equirectangular | boolean |           |          | 
 record_id       | integer |           | not null | 
Indexes:
    "photo_pkey" PRIMARY KEY, btree (record_id)
Foreign-key constraints:
    "photo_record_fkey" FOREIGN KEY (record_id) REFERENCES record__c(id) ON DELETE CASCADE
```

## Tips

### PostgreSQL table initialization
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

