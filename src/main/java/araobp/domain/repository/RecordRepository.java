package araobp.domain.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import araobp.domain.entity.Record;

public interface RecordRepository extends CrudRepository<Record, Integer> {

	@Query("SELECT id FROM record WHERE id = :id")
	public Iterable<Record> checkIfIdExists(@Param("id") Integer id);
	
	@Query("SELECT * FROM record ORDER BY id DESC LIMIT :limit OFFSET :offset")
	public Iterable<Record> getRecords(@Param("limit") Integer limit, @Param("offset") Integer offset);
	
	// [Reference] https://www.baeldung.com/spring-data-jpa-modifying-annotation
	@Modifying
	@Query("UPDATE record SET place = :place, memo = :memo WHERE id = :id")
	public Integer updateRecord(@Param("id") Integer id, @Param("place") String place, @Param("memo") String memo);
}
