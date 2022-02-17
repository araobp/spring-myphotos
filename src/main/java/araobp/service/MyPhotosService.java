package araobp.service;

import java.util.Optional;

public interface MyPhotosService {
	
	Iterable<Record> selectAll();
	
	Optional<Record> selectOneById(Integer id);
	
	void insertRecord(Record record);
	
	void updateRecord(Record record);
	
	void deleteRecordById(Integer id);
}
