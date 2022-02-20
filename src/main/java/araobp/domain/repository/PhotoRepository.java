package araobp.domain.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import araobp.domain.entity.Photo;

public interface PhotoRepository extends CrudRepository<Photo, Integer> {

	@Query("SELECT thumbnail FROM photo WHERE record_id = :id")
	public Optional<Photo> selectThumbnailById(@Param("id") Integer id);

	@Query("SELECT image FROM photo WHERE record_id = :id")
	public Optional<Photo> selectImageById(@Param("id") Integer id);

	@Query("INSERT INTO photo (record_id, image, thumbnail) VALUES (:id, :image, :thumbnail)")
	@Modifying
	public Integer insertImageAndThumbnail(
			@Param("id") Integer id,
			@Param("image") byte[] image,
			@Param("thumbnail") byte[] thumbnail
			);
}
