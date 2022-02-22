package araobp.domain.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.StreamSupport;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import araobp.domain.entity.Photo;
import araobp.domain.entity.Record;
import araobp.domain.repository.PhotoRepository;
import araobp.domain.repository.RecordRepository;
import net.coobird.thumbnailator.Thumbnails;

@Service
@Transactional
public class MyPhotosServiceImpl implements MyPhotosService {

	static final Logger logger = LogManager.getLogger(MyPhotosServiceImpl.class);

	static final Integer THUMBNAIL_TARGET_WIDTH = 128;

	@Autowired
	RecordRepository recordRepository;

	@Autowired
	PhotoRepository photoRepository;

	@Override
	public Optional<Record> selectRecordById(Integer id) {
		return recordRepository.findById(id);
	}

	@Override
	public Iterable<Record> selectRecords(Integer limit, Integer offset) {
		return recordRepository.getRecords(limit, offset);
	}

	@Override
	public Integer insertRecord(Record record) {
		record.setId(null);
		String datetime = Instant.now().toString(); // UTC
		record.setDatetime(datetime);
		Record r = recordRepository.save(record);
		return r.getId();
	}

	@Override
	public Boolean updateRecord(Integer id, String place, String memo) {
		int affectedRows = recordRepository.updateRecord(id, place, memo);
		return (affectedRows == 1) ? true : false;
	}

	@Override
	public void deleteRecordAndImageById(Integer id) {
		// Note: photo is also deleted by "ON DELETE CASCADE" setting on POSTGRES SQL
		// photoRepository.deleteById(id);
		recordRepository.deleteById(id);
	}

	@Override
	public byte[] selectThumbnailById(Integer id) {
		Optional<Photo> photo = photoRepository.selectThumbnailById(id);
		return photo.isPresent() ? photo.get().getThumbnail() : null;
	}

	@Override
	public byte[] selectImageById(Integer id) {
		Optional<Photo> photo = photoRepository.selectImageById(id);
		return photo.isPresent() ? photo.get().getImage() : null;
	}

	@Override
	public Boolean insertImage(Integer id, byte[] image) {
		if (checkIfIdExists(id)) {
			InputStream inputStream = new ByteArrayInputStream(image);
			BufferedImage originalImage;
			try {
				originalImage = ImageIO.read(inputStream);
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int targetHeight = originalImage.getHeight() * THUMBNAIL_TARGET_WIDTH / originalImage.getWidth();
			try {
				// Resize image
				Thumbnails.of(originalImage).size(THUMBNAIL_TARGET_WIDTH, targetHeight).outputFormat("JPEG").outputQuality(1)
						.toOutputStream(outputStream);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			byte[] thumbnail = outputStream.toByteArray();
			Integer affectedRows = photoRepository.insertImageAndThumbnail(id, image, thumbnail);
			return (affectedRows == 1) ? true : false;
		} else {
			return false;
		}
	}

	@Override
	public Boolean checkIfIdExists(Integer id) {
		Iterable<Record> records = recordRepository.checkIfIdExists(id);
		logger.info(records);
		Long count = StreamSupport.stream(records.spliterator(), false).count();
		return (count == 1) ? true : false;
	}
}
