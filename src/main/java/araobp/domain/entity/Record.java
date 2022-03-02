package araobp.domain.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {

	@Id
	private Integer id;
	private String datetime;
	private String place;
	private String memo;
	private Double latitude;
	private Double longitude;
	private String address;
}
