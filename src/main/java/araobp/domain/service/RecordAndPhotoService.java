package araobp.domain.service;

import java.util.Optional;

import araobp.domain.entity.Count;
import araobp.domain.entity.Id;
import araobp.domain.entity.PhotoAttribute;
import araobp.domain.entity.Record;

public interface RecordAndPhotoService {
	
	Boolean checkIfIdExists(Integer id);
	
	Iterable<Record> selectRecords(Integer limit, Integer offset);
	
	Optional<Record> selectRecordById(Integer id);
	
	Id insertRecord(Record record);
	
	Boolean updateRecord(Integer id, String place, String memo);
	
	void deleteRecordAndImageById(Integer id);
	
	byte[] selectThumbnailById(Integer id);

	byte[] selectImageById(Integer id);
	
	PhotoAttribute selectPhotoAttributeById(Integer id);
	
	Boolean insertImage(Integer id, byte[] bytes);
	
	Optional<Id> selectHeadId();
	
	Optional<Id> selectTailId();
	
	Count count();
}
