package araobp.domain.repository;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import araobp.domain.entity.Record;
import araobp.domain.entity.RecordEveryNth;

public interface RecordRepository extends CrudRepository<Record, Integer> {

	@Query("SELECT id FROM record WHERE id = :id")
	public Iterable<Record> checkIfIdExists(@Param("id") Integer id);
	
	@Query("SELECT * FROM record ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
	public Iterable<Record> getRecords(@Param("limit") Integer limit, @Param("offset") Integer offset);
	
	@Query("SELECT *, ( 6371 * ACOS( COS( RADIANS( :latitude ) ) * COS( RADIANS( latitude ) ) * COS( RADIANS( longitude ) - RADIANS( :longitude) ) + SIN( RADIANS( :latitude ) ) * SIN( RADIANS( latitude ) ) ) ) AS distance FROM record ORDER BY distance LIMIT :limit OFFSET :offset")
	public Iterable<Record> getRecordsClosestOrder(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("limit") Integer limit, @Param("offset") Integer offset);
	
	@Query("SELECT * FROM (SELECT id, timestamp, place, row_number() OVER (ORDER BY timestamp DESC) AS ROW FROM record) t WHERE t.row % :limit = 1")
	public Iterable<RecordEveryNth> getRecordsEveryNth(@Param("limit") Integer limit);
	
	@Query("SELECT * FROM ( SELECT *, row_number() OVER (ORDER BY distance) AS row FROM ( SELECT  *, ( 6371 * ACOS( COS( RADIANS( :latitude ) ) * COS( RADIANS( latitude ) ) * COS( RADIANS( longitude ) - RADIANS( :longitude ) ) + SIN( RADIANS( :latitude) ) * SIN( RADIANS( latitude ) ) ) ) AS distance FROM record ) withDistance ) withRow WHERE row % :limit = 1")
	public Iterable<RecordEveryNth> getRecordsEveryNthClosestOrder(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("limit") Integer limit);
	
	// [Reference] https://www.baeldung.com/spring-data-jpa-modifying-annotation
	@Modifying
	@Query("UPDATE record SET place = :place, memo = :memo WHERE id = :id")
	public Integer updateRecord(@Param("id") Integer id, @Param("place") String place, @Param("memo") String memo);

	@Modifying
	@Query("UPDATE record SET timestamp = :timestamp WHERE id = :id")
	public Integer updateTimestamp(@Param("id") Integer id, @Param("timestamp") Timestamp timestamp);
	
	@Modifying
	@Query("UPDATE record SET address = :address WHERE id = :id")
	public Integer updateAddress(@Param("id") Integer id, @Param("address") String address);

	@Modifying
	@Query("UPDATE record SET latitude = :latitude, longitude = :longitude WHERE id = :id")
	public Integer updateLatLon(@Param("id") Integer id, @Param("latitude") double latitude, @Param("longitude") double longitude);
	
	@Query("SELECT id FROM record ORDER BY id LIMIT 1")
	public Optional<Record> getHeadId();

	@Query("SELECT id FROM record ORDER BY id DESC LIMIT 1")
	public Optional<Record> getTailId();
	
	@Query("SELECT COUNT(id) FROM record")
	public long count();	
	
}
