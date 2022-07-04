package araobp.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("photo")
public class Photo {

	@Id
	private String uuid;
	private byte[] thumbnail;
	private byte[] image;
	private boolean equirectangular;
}
