package araobp.domain.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adobe.internal.xmp.XMPIterator;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.xmp.XmpDirectory;

import araobp.domain.entity.Count;
import araobp.domain.entity.Photo;
import araobp.domain.entity.PhotoAttribute;
import araobp.domain.entity.Record__c;
import araobp.domain.entity.Uuid;
import araobp.domain.entity.RecordEveryNth;
import araobp.domain.entity.RecordWithDistance;
import araobp.domain.repository.PhotoRepository;
import araobp.domain.repository.RecordRepository;
import araobp.nominatim.Nominatim;
import net.coobird.thumbnailator.Thumbnails;

@Service
@Transactional
public class RecordAndPhotoServiceImpl implements RecordAndPhotoService {

	static final Logger logger = LogManager.getLogger(RecordAndPhotoServiceImpl.class);

	static final Integer THUMBNAIL_TARGET_WIDTH = 128;

	static final Integer UTC_OFFSET = getUtcOffset();

	static Integer getUtcOffset() {
		String utcOffset = System.getenv().get("UTC_OFFSET");
		if (utcOffset == null)
			utcOffset = "9";
		return Integer.parseInt(utcOffset);
	}

	@Autowired
	RecordRepository recordRepository;

	@Autowired
	PhotoRepository photoRepository;

	@Autowired
	Nominatim nominatim;

	@Override
	public Optional<Record__c> selectRecordByUUID(String uuid) {
		return recordRepository.getRecordByUUID(uuid);
	}

	@Override
	public Iterable<RecordWithDistance> selectRecords(Integer limit, Integer offset) {
		return recordRepository.getRecords(limit, offset);
	}

	@Override
	public Iterable<RecordEveryNth> selectRecordsEveryNth(Integer limit) {
		return recordRepository.getRecordsEveryNth(limit);
	}

	@Override
	public Iterable<RecordWithDistance> selectRecordsClosestOrder(Double langitude, Double longitude, Integer limit,
			Integer offset) {
		return recordRepository.getRecordsClosestOrder(langitude, longitude, limit, offset);
	}

	@Override
	public Iterable<RecordEveryNth> selectRecordsEveryNthClosestOrder(Double langitude, Double longitude,
			Integer limit) {
		return recordRepository.getRecordsEveryNthClosestOrder(langitude, longitude, limit);
	}

	@Override
	public Uuid insertRecord(Record__c record) {
		String uuid = UUID.randomUUID().toString();
		record.setUuid_id__c(uuid);
		Timestamp timestamp = Timestamp.from(Instant.now());
		record.setTimestamp__c(timestamp);
		Record__c r = recordRepository.save(record);
		return new Uuid(uuid);
	}

	@Override
	public Boolean updateRecord(String uuid, String place, String memo) {
		int affectedRows = recordRepository.updateRecord(uuid, place, memo);
		return (affectedRows == 1);
	}

	@Override
	public Boolean deleteRecordAndImageByUUID(String uuid) {
		// Note: photo record will also be deleted by "ON DELETE CASCADE" setting.
		int affectedRows = recordRepository.deleteRecord(uuid);
		return (affectedRows == 1);
	}

	@Override
	public byte[] selectThumbnailByUUID(String uuid) {
		Optional<Photo> photo = photoRepository.selectThumbnailByUUID(uuid);
		return photo.isPresent() ? photo.get().getThumbnail() : null;
	}

	@Override
	public byte[] selectImageByUUID(String uuid) {
		Optional<Photo> photo = photoRepository.selectImageByUUID(uuid);
		return photo.isPresent() ? photo.get().getImage() : null;
	}

	@Override
	public PhotoAttribute selectPhotoAttributeByUUID(String uuid) {
		PhotoAttribute attr = null;
		try {
			Optional<Photo> photo = photoRepository.selectAttributeByUUID(uuid);
			if (photo.isPresent()) {
				attr = new PhotoAttribute();
				attr.setEquirectangular(photo.get().isEquirectangular());
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return attr;
	}

	@Override
	public Boolean insertImage(String uuid, byte[] image) {
		if (checkIfUUIDExists(uuid)) {
			InputStream inputStream = new ByteArrayInputStream(image);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			try {
				// Extract info from EXIF
				Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

				JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
				Integer height = jpegDirectory.getInteger(JpegDirectory.TAG_IMAGE_HEIGHT);
				Integer width = jpegDirectory.getInteger(JpegDirectory.TAG_IMAGE_WIDTH);
				int targetHeight = height * THUMBNAIL_TARGET_WIDTH / width;

				// Check if this image is equirectangular
				boolean equirectangular = false;
				for (XmpDirectory xmpDirectory : metadata.getDirectoriesOfType(XmpDirectory.class)) {
					XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
					XMPIterator itr = xmpMeta.iterator();

					while (itr.hasNext()) {
						XMPPropertyInfo property = (XMPPropertyInfo) itr.next();
						String path = property.getPath();
						if (path != null && property.getPath().equals("GPano:ProjectionType")) {
							equirectangular = true;
						}
					}
				}

				// Try to get geo location from the image
				String address = "";
				GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
				if (gpsDirectory != null) {
					GeoLocation geoLocation = gpsDirectory.getGeoLocation();
					Double latitude = geoLocation.getLatitude();
					Double longitude = geoLocation.getLongitude();
					if (latitude != null && latitude != 0.0 && longitude != null && longitude != 0.0) {
						recordRepository.updateLatLon(uuid, latitude, longitude);
						address = nominatim.getAddress(latitude, longitude);
						recordRepository.updateAddress(uuid, address);
					}
				}

				// Try to get datetime from the image
				ExifSubIFDDirectory exifSubIfdDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
				if (exifSubIfdDirectory != null) {
					Date date = exifSubIfdDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
					if (date != null) {
						Instant instant = date.toInstant();
						instant = instant.minus(UTC_OFFSET, ChronoUnit.HOURS); // EXIF datetime does not take time zone
																				// into
																				// account
						Timestamp timestamp = Timestamp.from(instant);
						recordRepository.updateTimestamp(uuid, timestamp);
					}
				}

				// Resize image
				// [Reference] https://github.com/coobird/thumbnailator/issues/43
				inputStream.reset();
				Thumbnails.of(inputStream).size(THUMBNAIL_TARGET_WIDTH, targetHeight).outputFormat("JPEG")
						.outputQuality(1).toOutputStream(outputStream);

				// Insert the image data to photo table
				byte[] thumbnail = outputStream.toByteArray();
				Integer affectedRows = photoRepository.insertImageAndThumbnail(uuid, image, thumbnail, equirectangular);
				
				return (affectedRows == 1) ? true : false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public Boolean checkIfUUIDExists(String uuid) {
		Iterable<Record__c> records = recordRepository.checkIfUUIDExists(uuid);
		// logger.info(records);
		Long count = StreamSupport.stream(records.spliterator(), false).count();
		return (count == 1) ? true : false;
	}

	@Override
	public Optional<Uuid> selectHeadUUID() {
		Optional<Record__c> record = recordRepository.getHeadUUID();
		Uuid uuid = null;
		if (record.isPresent()) {
			uuid = new Uuid();
			uuid.setUuid(record.get().getUuid());
		}
		return Optional.ofNullable(uuid);
	}

	@Override
	public Optional<Uuid> selectTailUUID() {
		Optional<Record__c> record = recordRepository.getTailUUID();
		Uuid uuid = null;
		if (record.isPresent()) {
			uuid = new Uuid();
			uuid.setUuid(record.get().getUuid());
		}
		return Optional.ofNullable(uuid);
	}

	@Override
	public Count count() {
		long count = recordRepository.count();
		return new Count(count);
	}

}
