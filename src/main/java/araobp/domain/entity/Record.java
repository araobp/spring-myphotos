package araobp.domain.entity;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {

	@Id
	private Integer id;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Timestamp timestamp;
	private String place;
	private String memo;
	private Double latitude;
	private Double longitude;
	private String address;
}
