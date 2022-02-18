package araobp;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import araobp.domain.entity.Record;
import araobp.domain.service.MyPhotosService;

@SpringBootApplication
public class MyPhotosApplication {

	static final Logger logger = LogManager.getLogger(MyPhotosApplication.class);

	@Autowired
	MyPhotosService service;

	public static void main(String[] args) {
		SpringApplication.run(MyPhotosApplication.class, args);
		// SpringApplication.run(MyPhotosApplication.class, args).getBean(MyPhotosApplication.class).test();
	}

	private void test() {
		

		Record record1 = new Record(null, null, "Yokohama", "Landmark tower", 35.45517244565901, 139.63140199972472);
		Record record2 = new Record(null, null, "Kawasaki", "LAZONA Kawasaki plaza", 35.53275879368219,
				139.69589252873195);

		service.insert(record1);
		service.insert(record2);
		
		Iterable<Record> records = service.selectAll();
		for (Record r : records) {
			logger.info(r);
		}		
	}

}
