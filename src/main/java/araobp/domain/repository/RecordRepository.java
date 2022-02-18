package araobp.domain.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import araobp.domain.entity.Record;

public interface RecordRepository extends CrudRepository<Record, Integer> {

	@Query("UPDATE record SET place = :place, memo = :memo WHERE id = :id")
	public void updateRecord(@Param("id") Integer id, @Param("place") String place, @Param("memo") String memo);
}
