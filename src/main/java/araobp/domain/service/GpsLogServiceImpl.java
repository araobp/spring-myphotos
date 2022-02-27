package araobp.domain.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import araobp.domain.entity.Count;
import araobp.domain.entity.GpsLog;
import araobp.domain.entity.Id;
import araobp.domain.repository.GpsLogRepository;

@Service
@Transactional
public class GpsLogServiceImpl implements GpsLogService {

	@Autowired
	GpsLogRepository gpsLogRepository;

	@Override
	public Id insertGpsLog(GpsLog gpsLog) {
		gpsLog.setId(null);
		String datetime = Instant.now().toString(); // UTC
		gpsLog.setDatetime(datetime);
		GpsLog g = gpsLogRepository.save(gpsLog);
		if (gpsLog.getSession() == null) {
			g.setSession(g.getId());
			g = gpsLogRepository.save(gpsLog);
		}
		return new Id(g.getId());
	}
		
	@Override
	public Count countSessions() {
		long count = gpsLogRepository.count();
		return new Count(count);
	}
	
	@Override
	public Iterable<GpsLog> getNextSession(Integer current) {
		return gpsLogRepository.getNextSession(current);
	}
}
