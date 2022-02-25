package araobp.domain.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import araobp.domain.entity.GpsLog;

public interface GpsLogRepository extends CrudRepository<GpsLog, Integer> {

	@Query("SELECT * FROM gps_log ORDER BY id DESC LIMIT :limit OFFSET :offset")
	public Iterable<GpsLog> getRecords(@Param("limit") Integer limit, @Param("offset") Integer offset);

	@Query("SELECT COUNT(id) FROM gps_log")
	public long count();
}
