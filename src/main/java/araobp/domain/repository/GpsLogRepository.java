package araobp.domain.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import araobp.domain.entity.GpsLog;

public interface GpsLogRepository extends CrudRepository<GpsLog, Integer> {

	@Query("SELECT * FROM gps_log where session = (SELECT MAX(session) FROM gps_log WHERE session < :current")
	public Iterable<GpsLog> getNextSession(@Param("current") Integer session);
	
	@Query("SELECT COUNT(session) FROM (SELECT DISTINCT session FROM gps_log) AS TEMP")
	public long countSessions();	
}
