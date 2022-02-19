package araobp.domain.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

	@Id
	private Integer recordId;
	private byte[] thumbnail;
	private byte[] image;
}
