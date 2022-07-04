package araobp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import araobp.domain.service.RecordAndPhotoService;

@SpringBootApplication
public class MyPhotosApplication {

	static final Logger logger = LogManager.getLogger(MyPhotosApplication.class);

	@Autowired
	RecordAndPhotoService service;

	public static void main(String[] args) {
		SpringApplication.run(MyPhotosApplication.class, args);
	}
}
