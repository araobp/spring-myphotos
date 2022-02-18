package araobp.domain.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import araobp.domain.entity.Record;
import araobp.domain.repository.RecordRepository;

@Service
@Transactional
public class MyPhotosServiceImpl implements MyPhotosService {

	@Autowired
	RecordRepository repository;
	
	@Override
	public Iterable<Record> selectAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Record> selectOneById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public void insert(Record record) {
		record.setId(null);
		String datetime = Instant.now().toString();  // UTC
		record.setDatetime(datetime);
		repository.save(record);
	}

	@Override
	public void update(Integer id, String place, String memo) {
		repository.updateRecord(id, place, memo);
	}

	@Override
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}
}
