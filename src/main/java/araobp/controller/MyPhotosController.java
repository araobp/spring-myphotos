package araobp.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import araobp.domain.service.MyPhotosService;
import araobp.domain.entity.Record;

@Controller
public class MyPhotosController {

	@Autowired
	MyPhotosService service;

	@GetMapping("/album")
	public String showAlbum(@RequestParam Integer limit, @RequestParam Integer offset, Model model) {

		Iterable<Record> records = service.selectRecords(limit, offset);

		for (Record r : records) {
			String utc = r.getDatetime();
			if (utc != null) {
				Instant instant = Instant.parse(utc);
				LocalDateTime datetime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedDateTime = datetime.format(formatter);
				r.setDatetime(formattedDateTime);
			} else {
				r.setDatetime("- - -");
			}
		}

		model.addAttribute("records", records);

		return "album.html";
	}
}
