package araobp.domain.service;

import java.util.Optional;

import araobp.domain.entity.Record;

public interface MyPhotosService {
	
	//Iterable<Record> selectAll();
	
	Iterable<Record> selectAllRecords(Integer limit, Integer offset);
	
	Optional<Record> selectRecordById(Integer id);
	
	Integer insertRecord(Record record);
	
	Boolean updateRecord(Integer id, String place, String memo);
	
	void deleteRecordById(Integer id);
	
	byte[] selectThumbnailById(Integer id);

	byte[] selectImageById(Integer id);
}
