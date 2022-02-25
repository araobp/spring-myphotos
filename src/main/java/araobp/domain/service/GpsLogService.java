package araobp.domain.service;

import araobp.domain.entity.Count;
import araobp.domain.entity.GpsLog;

public interface  GpsLogService {

	Integer insertGpsLog(GpsLog gpsLog);
	
	Iterable<GpsLog> selectGpsLogs(Integer limit, Integer offset);

	Count count();
}
