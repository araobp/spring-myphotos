package araobp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import araobp.domain.service.RecordAndPhotoService;

@Controller
public class MyPhotosController {

	static final Integer LIMIT = 2147483647;
	static final Integer OFFSET = 0;
	
	@Autowired
	RecordAndPhotoService service;

	@GetMapping("/")
	public String showIndex(Model model) {
		return "index";
	}
	
	@GetMapping("/album")
	public String showAlbum(Model model) {
		return "album";
	}
}
