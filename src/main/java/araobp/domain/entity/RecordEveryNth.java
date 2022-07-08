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
public class RecordEveryNth {

	@Id
	private String uuid__c;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Timestamp timestamp__c;
	private String name;
	private Double distance;
}