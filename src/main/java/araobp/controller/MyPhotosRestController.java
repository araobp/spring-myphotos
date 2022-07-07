package araobp.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import araobp.domain.entity.Count;
import araobp.domain.entity.PhotoAttribute;
import araobp.domain.entity.Record__c;
import araobp.domain.entity.Uuid;
import araobp.domain.entity.RecordEveryNth;
import araobp.domain.entity.RecordWithDistance;
import araobp.domain.service.RecordAndPhotoService;

@RestController
public class MyPhotosRestController {

	static final Logger logger = LogManager.getLogger(MyPhotosRestController.class);

	static final String NOT_FOUND_REASON = "ID not found";

	public static final String PREVIOUS = "previous";
	public static final String NEXT = "next";
	
	@Autowired
	RecordAndPhotoService recordAndPhotoService;
		
	@PostMapping("/record")
	public Uuid postRecord(@RequestBody Record__c record, @RequestParam Integer timezone ) {
		return recordAndPhotoService.insertRecord(record, timezone);
	}
	
	@PatchMapping("/record/{uuid}")
	public void patchRecord(@PathVariable String uuid, @RequestBody Record__c record) {
		Boolean success = recordAndPhotoService.updateRecord(uuid, record.getName(), record.getMemo__c());
		if (!success) throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
	}
	
	@DeleteMapping("/record/{uuid}")
	public void deleteRecord(@PathVariable String uuid) {
		recordAndPhotoService.deleteRecordAndImageByUUID(uuid);
	}
	
	@GetMapping("/record")
	public Iterable<RecordWithDistance> getRecords(@RequestParam(required = false) Double latitude, @RequestParam(required = false) Double longitude, @RequestParam Integer limit, @RequestParam Integer offset) {
		Iterable<RecordWithDistance> records;
		if (latitude != null&& longitude != null) {
			records = recordAndPhotoService.selectRecordsClosestOrder(latitude, longitude, limit, offset);						
		} else {
			records = recordAndPhotoService.selectRecords(limit, offset);			
		}
		return records;
	}
		
	@GetMapping("/record/{uuid}")
	public Optional<Record__c> getRecord(@PathVariable String uuid) {
		Optional<Record__c> record = recordAndPhotoService.selectRecordByUUID(uuid);
		if (record.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
		}
		return record;
	}
	
	@GetMapping("/photo/{uuid}/attribute")
	public PhotoAttribute getPhotoAttribute(@PathVariable String uuid) {
		PhotoAttribute attr = recordAndPhotoService.selectPhotoAttributeByUUID(uuid);
		if (attr == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
		}
		return attr;
	}

	@GetMapping(value = "/photo/{uuid}/thumbnail", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getThumbnail(@PathVariable String uuid) throws ResponseStatusException {
		byte[] thumbnail = recordAndPhotoService.selectThumbnailByUUID(uuid);
		if (thumbnail == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
		}
		return thumbnail;
	}

	@GetMapping(value = "/photo/{uuid}/image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getImage(@PathVariable String uuid) throws ResponseStatusException {
		byte[] image = recordAndPhotoService.selectImageByUUID(uuid);
		if (image == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
		}
		return image;
	}
	
	@PostMapping("/photo/{uuid}")
	public void postImage(@PathVariable String uuid, @RequestBody byte[] image, @RequestParam Integer timezone) {
	    Boolean success = recordAndPhotoService.insertImage(uuid, image, timezone);
		if (!success) throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_REASON);
	}
	
	@GetMapping("/management/record/count")
	public Count recordCount() throws ResponseStatusException {
		return recordAndPhotoService.count();
	}
	
	@GetMapping("/management/record/everynth")
	public Iterable<RecordEveryNth> getRecordsEveryNth(@RequestParam(required = false) Double latitude, @RequestParam(required = false) Double longitude, @RequestParam Integer limit) {
		Iterable<RecordEveryNth> records;
		if (latitude != null && longitude != null) {
			records = recordAndPhotoService.selectRecordsEveryNthClosestOrder(latitude, longitude, limit);
		} else {
			records = recordAndPhotoService.selectRecordsEveryNth(limit);
		}
		return records;
	}
}
