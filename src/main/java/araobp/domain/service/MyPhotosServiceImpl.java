package araobp.domain.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import araobp.domain.entity.Photo;
import araobp.domain.entity.Record;
import araobp.domain.repository.PhotoRepository;
import araobp.domain.repository.RecordRepository;

@Service
@Transactional
public class MyPhotosServiceImpl implements MyPhotosService {

	@Autowired
	RecordRepository recordRepository;

	@Autowired
	PhotoRepository photoRepository;

	@Override
	public Iterable<Record> selectAll() {
		return recordRepository.findAll();
	}

	@Override
	public Optional<Record> selectOneById(Integer id) {
		return recordRepository.findById(id);
	}
	
	@Override
	public Iterable<Record> getRecords(Integer limit, Integer offset) {
		return recordRepository.getRecords(limit, offset);
	}

	@Override
	public Integer insert(Record record) {
		record.setId(null);
		String datetime = Instant.now().toString(); // UTC
		record.setDatetime(datetime);
		Record r = recordRepository.save(record);
		return r.getId();
	}

	@Override
	public void update(Integer id, String place, String memo) {
		recordRepository.updateRecord(id, place, memo);
	}

	@Override
	public void deleteById(Integer id) {
		recordRepository.deleteById(id);
	}

	@Override
	public byte[] selectThumbnailById(Integer id) {
		Optional<Photo> photo = photoRepository.selectThumbnailById(id);
		return photo.isPresent()? photo.get().getThumbnail() : null;
	}

	@Override
	public byte[] selectImageById(Integer id) {
		Optional<Photo> photo = photoRepository.selectImageById(id);
		return photo.isPresent()? photo.get().getImage(): null;
	}
}
