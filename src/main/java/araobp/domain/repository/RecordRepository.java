package araobp.domain.repository;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import araobp.domain.entity.Record__c;
import araobp.domain.entity.RecordEveryNth;
import araobp.domain.entity.RecordWithDistance;

public interface RecordRepository extends CrudRepository<Record__c, Integer> {

	@Query("SELECT uuid FROM record__c WHERE uuid = :uuid")
	public Iterable<Record__c> checkIfUUIDExists(@Param("uuid") String uuid);
	
	@Query("SELECT * FROM record__c WHERE uuid = :uuid")
	public Optional<Record__c> getRecordByUUID(@Param("uuid") String uuid);
	
	@Query("SELECT * FROM record__c ORDER BY timestamp__c DESC LIMIT :limit OFFSET :offset")
	public Iterable<RecordWithDistance> getRecords(@Param("limit") Integer limit, @Param("offset") Integer offset);
	
	@Query("SELECT *, ( 6371 * ACOS( COS( RADIANS( :latitude ) ) * COS( RADIANS( geolocation__latitude__s ) ) * COS( RADIANS( geolocation__longitude__s ) - RADIANS( :longitude) ) + SIN( RADIANS( :latitude ) ) * SIN( RADIANS( geolocation__latitude__s ) ) ) ) AS distance FROM record__c ORDER BY distance LIMIT :limit OFFSET :offset")
	public Iterable<RecordWithDistance> getRecordsClosestOrder(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("limit") Integer limit, @Param("offset") Integer offset);
	
	@Query("SELECT * FROM (SELECT id, timestamp__c, name, row_number() OVER (ORDER BY timestamp__c DESC) AS ROW FROM record__c) t WHERE t.row % :limit = 1")
	public Iterable<RecordEveryNth> getRecordsEveryNth(@Param("limit") Integer limit);
	
	@Query("SELECT * FROM ( SELECT *, row_number() OVER (ORDER BY distance) AS row FROM ( SELECT  *, ( 6371 * ACOS( COS( RADIANS( :latitude ) ) * COS( RADIANS( geolocation__latitude__s ) ) * COS( RADIANS( geolocation__longitude__s ) - RADIANS( :longitude ) ) + SIN( RADIANS( :latitude) ) * SIN( RADIANS( geolocation__latitude__s ) ) ) ) AS distance FROM record__c ) withDistance ) withRow WHERE row % :limit = 1")
	public Iterable<RecordEveryNth> getRecordsEveryNthClosestOrder(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("limit") Integer limit);
	
	// [Reference] https://www.baeldung.com/spring-data-jpa-modifying-annotation
	@Modifying
	@Query("UPDATE record__c SET name = :place, memo__c = :memo WHERE uuid = :uuid")
	public Integer updateRecord(@Param("uuid") String uuid, @Param("place") String place, @Param("memo") String memo);

	@Modifying
	@Query("UPDATE record__c SET timestamp__c = :timestamp WHERE uuid = :uuid")
	public Integer updateTimestamp(@Param("uuid") String uuid, @Param("timestamp") Timestamp timestamp);
	
	@Modifying
	@Query("UPDATE record__c SET address__c = :address WHERE uuid = :uuid")
	public Integer updateAddress(@Param("uuid") String uuid, @Param("address") String address);

	@Modifying
	@Query("UPDATE record__c SET geolocation__latitude__s = :latitude, geolocation__longitude__s = :longitude WHERE uuid = :uuid")
	public Integer updateLatLon(@Param("uuid") String uuid, @Param("latitude") double latitude, @Param("longitude") double longitude);
	
	@Modifying
	@Query("DELETE FROM record__c WHERE UUID = :uuid")
	public Integer deleteRecord(@Param("uuid") String uuid);
	
	@Query("SELECT uuid FROM record__c ORDER BY id LIMIT 1")
	public Optional<Record__c> getHeadUUID();

	@Query("SELECT uuid FROM record__c ORDER BY id DESC LIMIT 1")
	public Optional<Record__c> getTailUUID();
	
	@Query("SELECT COUNT(uuid) FROM record__c")
	public long count();	
	
}
