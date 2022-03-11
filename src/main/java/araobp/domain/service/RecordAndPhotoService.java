package araobp.domain.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import araobp.domain.entity.Count;
import araobp.domain.entity.Id;
import araobp.domain.entity.PhotoAttribute;
import araobp.domain.entity.Record;
import araobp.domain.entity.RecordEveryNth;

public interface RecordAndPhotoService {
	
	Boolean checkIfIdExists(Integer id);
	
	Iterable<Record> selectRecords(Integer limit, Integer offset);
	
	Iterable<RecordEveryNth> selectRecordsEveryNth(Integer limit);
	
	Iterable<Record> selectRecordsClosestOrder(Double latitude, Double longitude, Integer limit, Integer offset);
	
	Iterable<RecordEveryNth> selectRecordsEveryNthClosestOrder(Double latitude, Double longitude, Integer limit);
	
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
