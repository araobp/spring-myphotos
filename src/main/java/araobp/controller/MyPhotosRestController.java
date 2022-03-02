package araobp.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import araobp.domain.entity.Count;
import araobp.domain.entity.GpsLog;
import araobp.domain.entity.Id;
import araobp.domain.entity.PhotoAttribute;
import araobp.domain.entity.Record;
import araobp.domain.service.GpsLogService;
import araobp.domain.service.RecordAndPhotoService;

@RestController
public class MyPhotosRestController {

	static final Logger logger = LogManager.getLogger(MyPhotosRestController.class);

	static final String NOT_FOUND_REASON = "ID not found";

	public static final String PREVIOUS = "previous";
	public static final String NEXT = "next";
	
	@Autowired
	RecordAndPhotoService recordAndPhotoService;
	
	@Autowired
	GpsLogService gpsLogService;
	
	@PostMapping("/record")
	public Id postRecord(@RequestBody Record record) {
		return recordAndPhotoService.insertRecord(record);
	}
	
	@PutMapping("/record/{id}")
	public void putRecord(@PathVariable Integer id, @RequestBody Record record) {
		Boolean success = recordAndPhotoService.updateRecord(id, record.getPlace(), record.getMemo());
		logger.info(success);
		if (!success) throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
	}
	
	@DeleteMapping("/record/{id}")
	public void deleteRecord(@PathVariable Integer id) {
		recordAndPhotoService.deleteRecordAndImageById(id);
	}
	
	@GetMapping("/record")
	public Iterable<Record> getRecords(@RequestParam Integer limit, @RequestParam Integer offset) {
		Iterable<Record> records = recordAndPhotoService.selectRecords(limit, offset);
		return records;
	}
	
	@GetMapping("/record/{id}")
	public Optional<Record> getRecord(@PathVariable Integer id) {
		Optional<Record> record = recordAndPhotoService.selectRecordById(id);
		if (record.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
		}
		return record;
	}
	
	@GetMapping("/photo/{id}/attribute")
	public PhotoAttribute getPhotoAttribute(@PathVariable Integer id) {
		PhotoAttribute attr = recordAndPhotoService.selectPhotoAttributeById(id);
		if (attr == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
		}
		return attr;
	}

	@GetMapping(value = "/photo/{id}/thumbnail", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getThumbnail(@PathVariable Integer id) throws ResponseStatusException {
		byte[] thumbnail = recordAndPhotoService.selectThumbnailById(id);
		if (thumbnail == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
		}
		return thumbnail;
	}

	@GetMapping(value = "/photo/{id}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getImage(@PathVariable Integer id) throws ResponseStatusException {
		byte[] image = recordAndPhotoService.selectImageById(id);
		if (image == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
		}
		return image;
	}
	
	@PostMapping("/photo/{id}")
	public void postImage(@PathVariable Integer id, @RequestBody byte[] image) {
	    Boolean success = recordAndPhotoService.insertImage(id, image);
		logger.info(success);
		if (!success) throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
	}
	
	@PostMapping("/gpslog")
	public Id postGpsLog(@RequestBody GpsLog gpsLog) {
		return gpsLogService.insertGpsLog(gpsLog);
	}
		
	@GetMapping("/gpslog")
	public Iterable<GpsLog> getSession(@RequestParam Integer current, @RequestParam String direction) {
		if (direction.equals(PREVIOUS)) {
			return gpsLogService.getPreviousSession(current);
		} else if (direction.equals(NEXT)){
			return gpsLogService.getNextSession(current);			
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal direction");
		}
	}
	
	@GetMapping("/management/record/count")
	public Count recordCount() throws ResponseStatusException {
		return recordAndPhotoService.count();
	}

	@GetMapping("/management/gpslog/count")
	public Count gpsLogCount() throws ResponseStatusException {
		return gpsLogService.countSessions();
	}
}
