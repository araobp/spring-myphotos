package araobp.domain.service;

import java.util.Optional;

import araobp.domain.entity.Id;
import araobp.domain.entity.Record;

public interface MyPhotosService {
	
	Boolean checkIfIdExists(Integer id);
	
	Iterable<Record> selectRecords(Integer limit, Integer offset);
	
	Optional<Record> selectRecordById(Integer id);
	
	Integer insertRecord(Record record);
	
	Boolean updateRecord(Integer id, String place, String memo);
	
	void deleteRecordAndImageById(Integer id);
	
	byte[] selectThumbnailById(Integer id);

	byte[] selectImageById(Integer id);
	
	Boolean insertImage(Integer id, byte[] bytes);
	
	Optional<Id> selectHeadId();
	
	Optional<Id> selectTailId();
	
	long count();
}
