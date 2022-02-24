package araobp.domain.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import araobp.domain.entity.GpsLog;
import araobp.domain.repository.GpsLogRepository;

@Service
@Transactional
public class GpsLogServiceImpl implements GpsLogService {

	@Autowired
	GpsLogRepository gpsLogRepository;

	@Override
	public Integer insertGpsLog(GpsLog gpsLog) {
		gpsLog.setId(null);
		String datetime = Instant.now().toString(); // UTC
		gpsLog.setDatetime(datetime);
		GpsLog g = gpsLogRepository.save(gpsLog);
		return g.getId();
	}

}