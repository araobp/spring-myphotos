package araobp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import araobp.domain.entity.Record;
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
