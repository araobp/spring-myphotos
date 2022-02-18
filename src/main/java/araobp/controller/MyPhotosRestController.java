package araobp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import araobp.domain.entity.Record;
import araobp.domain.service.MyPhotosService;

@RestController
public class MyPhotosRestController {

	@Autowired
	MyPhotosService service;
	
	@GetMapping("/records")
	public Iterable<Record> getRecords() {
		Iterable<Record> records = service.selectAll();
		return records;
	}

}
