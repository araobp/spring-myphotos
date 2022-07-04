package araobp.domain.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import araobp.domain.entity.Photo;

public interface PhotoRepository extends CrudRepository<Photo, Integer> {

	@Query("SELECT thumbnail FROM photo WHERE uuid = :uuid")
	public Optional<Photo> selectThumbnailByUUID(@Param("uuid") String uuid);

	@Query("SELECT image FROM photo WHERE uuid = :uuid")
	public Optional<Photo> selectImageByUUID(@Param("uuid") String uuid);

	@Modifying
	@Query("INSERT INTO photo (uuid, image, thumbnail, equirectangular) VALUES (:uuid, :image, :thumbnail, :equirectangular)")
	public Integer insertImageAndThumbnail(
			@Param("uuid") String uuid,
			@Param("image") byte[] image,
			@Param("thumbnail") byte[] thumbnail,
			@Param("equirectangular") boolean equirectangular
			);
	
	@Query("SELECT equirectangular FROM photo WHERE uuid = :uuid")
	public Optional<Photo> selectAttributeByUUID(@Param("uuid") String uuid);

}
