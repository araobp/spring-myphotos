package araobp.domain.service;

import java.util.Optional;

import araobp.domain.entity.Count;
import araobp.domain.entity.PhotoAttribute;
import araobp.domain.entity.Record__c;
import araobp.domain.entity.Uuid;
import araobp.domain.entity.RecordEveryNth;
import araobp.domain.entity.RecordWithDistance;

public interface RecordAndPhotoService {
	
	Boolean checkIfUUIDExists(String uuid);
		
	Iterable<RecordWithDistance> selectRecords(Integer limit, Integer offset);
	
	Iterable<RecordEveryNth> selectRecordsEveryNth(Integer limit);
	
	Iterable<RecordWithDistance> selectRecordsClosestOrder(Double latitude, Double longitude, Integer limit, Integer offset);
	
	Iterable<RecordEveryNth> selectRecordsEveryNthClosestOrder(Double latitude, Double longitude, Integer limit);
	
	Optional<Record__c> selectRecordByUUID(String uuid);
	
	Uuid insertRecord(Record__c record);
	
	Boolean updateRecord(String uuid, String place, String memo);
	
	Boolean deleteRecordAndImageByUUID(String uuid);
	
	byte[] selectThumbnailByUUID(String id);

	byte[] selectImageByUUID(String uuid);
	
	PhotoAttribute selectPhotoAttributeByUUID(String uuid);
	
	Boolean insertImage(String uuid, byte[] bytes);
	
	Optional<Uuid> selectHeadUUID();
	
	Optional<Uuid> selectTailUUID();
	
	Count count();

}
