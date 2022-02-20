package araobp.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import araobp.domain.entity.Id;
import araobp.domain.entity.Record;
import araobp.domain.service.MyPhotosService;

@RestController
public class MyPhotosRestController {

	static final Logger logger = LogManager.getLogger(MyPhotosRestController.class);

	@Autowired
	MyPhotosService service;
	
	@PostMapping("/record")
	public Id postRecord(@RequestBody Record record) {
		Integer id = service.insertRecord(record);
		return new Id(id);
	}
	
	@PutMapping("/record/{id}")
	public void putRecord(@PathVariable Integer id, @RequestBody Record record) {
		Boolean success = service.updateRecord(id, record.getPlace(), record.getMemo());
		logger.info(success);
		if (!success) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/record/{id}")
	public void deleteRecord(@PathVariable Integer id) {
		service.deleteRecordAndImageById(id);
	}
	
	@GetMapping("/record")
	public Iterable<Record> getRecords(@RequestParam Integer limit, @RequestParam Integer offset) {
		Iterable<Record> records = service.selectRecords(limit, offset);
		return records;
	}
	
	@GetMapping("/record/{id}")
	public Optional<Record> getRecord(@PathVariable Integer id) {
		Optional<Record> record = service.selectRecordById(id);
		if (record.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return record;
	}

	@GetMapping(value = "/photo/{id}/thumbnail", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getThumbnail(@PathVariable Integer id) throws ResponseStatusException {
		byte[] thumbnail = service.selectThumbnailById(id);
		if (thumbnail == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return thumbnail;
	}

	@GetMapping(value = "/photo/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getImage(@PathVariable Integer id) throws ResponseStatusException {
		byte[] image = service.selectImageById(id);
		if (image == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return image;
	}
	
	@PostMapping("/photo/{id}")
	public void postImage(@PathVariable Integer id, @RequestBody byte[] image) {
	    Boolean success = service.insertImage(id, image);
		logger.info(success);
		if (!success) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
}
