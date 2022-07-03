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

## Data model definitions

[Step1] Create "record__c" custom by using Object Manager on Salesforce Platform.

[Step2] Add Heroku-Connect plugin on Heroku, then add a mapping of fields.

[Step3] Define "photo" table on PostgreSQL by using psql.

```
DATABASE=> set search_path to salesforce;                                                                                                     SET

DATABASE=> \d record__c
                                              Table "salesforce.record__c"
          Column           |            Type             | Collation | Nullable |                Default                
---------------------------+-----------------------------+-----------+----------+---------------------------------------
 geolocation__latitude__s  | double precision            |           |          | 
 uuid__c                   | character varying(32)       |           |          | 
 geolocation__longitude__s | double precision            |           |          | 
 name                      | character varying(80)       |           |          | 
 timestamp__c              | timestamp without time zone |           |          | 
 address__c                | character varying(200)      |           |          | 
 isdeleted                 | boolean                     |           |          | 
 systemmodstamp            | timestamp without time zone |           |          | 
 createddate               | timestamp without time zone |           |          | 
 sfid                      | character varying(18)       | ucs_basic |          | 
 id                        | integer                     |           | not null | nextval('record__c_id_seq'::regclass)
 _hc_lastop                | character varying(32)       |           |          | 
 _hc_err                   | text                        |           |          | 
Indexes:
    "record__c_pkey" PRIMARY KEY, btree (id)
    "hc_idx_record__c_systemmodstamp" btree (systemmodstamp)
    "hcu_idx_record__c_sfid" UNIQUE, btree (sfid)
    "hcu_idx_record__c_uuid__c" UNIQUE, btree (uuid__c)
Referenced by:
    TABLE "photo" CONSTRAINT "photo_uuid_fkey" FOREIGN KEY (uuid) REFERENCES record__c(uuid__c) ON DELETE CASCADE
Triggers:
    hc_record__c_logtrigger AFTER INSERT OR DELETE OR UPDATE ON record__c FOR EACH ROW WHEN (public.get_xmlbinary()::text = 'base64'::text) EXECUTE FUNCTION hc_record__c_logger()
    hc_record__c_status_trigger BEFORE INSERT OR UPDATE ON record__c FOR EACH ROW EXECUTE FU
    
DATABASE=> \d photo
                         Table "salesforce.photo"
     Column      |         Type          | Collation | Nullable | Default 
-----------------+-----------------------+-----------+----------+---------
 uuid            | character varying(32) |           | not null | 
 image           | bytea                 |           |          | 
 thumbnail       | bytea                 |           |          | 
 equirectangular | boolean               |           |          | 
Indexes:
    "photo_pkey" PRIMARY KEY, btree (uuid)
Foreign-key constraints:
    "photo_uuid_fkey" FOREIGN KEY (uuid) REFERENCES record__c(uuid__c) ON DELETE CASCADE
```

## Tips

### How to add "on delete cascade" constraints?

https://stackoverflow.com/questions/10356484/how-to-add-on-delete-cascade-constraints

### Resizing image preserving EXIF orientation by Thumnailator

Use an instance of ByteArrayInputStream as an input of Thumbnails.of():

```
InputStream inputStream = new ByteArrayInputStream(image);
Thumbnails.of(inputStream).size(THUMBNAIL_TARGET_WIDTH, targetHeight).outputFormat("JPEG")
    .outputQuality(1).toOutputStream(outputStream);
```

