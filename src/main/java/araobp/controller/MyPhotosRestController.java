package araobp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import araobp.domain.entity.Record;
import araobp.domain.service.MyPhotosService;

@RestController
public class MyPhotosRestController {

	@Autowired
	MyPhotosService service;
	
	@GetMapping("/record")
	public Iterable<Record> getRecords() {
		Iterable<Record> records = service.selectAll();
		return records;
	}
	
	@GetMapping("/record/{id}")
	public Optional<Record> getRecord(@PathVariable Integer id) {
		Optional<Record> record = service.selectOneById(id);
		if (record.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return record;
	}
	

}
