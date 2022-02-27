package araobp.domain.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpsLog {

	@Id
	private Integer id;
	private String datetime;
	private Double latitude;
	private Double longitude;
	private Integer session;

}
