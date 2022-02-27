package araobp.domain.service;

import araobp.domain.entity.Count;
import araobp.domain.entity.GpsLog;
import araobp.domain.entity.Id;

public interface  GpsLogService {

	Id insertGpsLog(GpsLog gpsLog);
	
	Count countSessions();
	
	Iterable<GpsLog> getNextSession(Integer current);
}
