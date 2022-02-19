package araobp.domain.service;

import java.util.Optional;

import araobp.domain.entity.Record;

public interface MyPhotosService {
	
	Iterable<Record> selectAll();
	
	Optional<Record> selectOneById(Integer id);
	
	Integer insert(Record record);
	
	void update(Integer id, String memo, String place);
	
	void deleteById(Integer id);
	
	byte[] selectThumbnailById(Integer id);

	byte[] selectImageById(Integer id);
}
