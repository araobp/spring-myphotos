package araobp.domain.entity;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("record__c")
public class Record__c {

	@Id
	private Integer id;

	private String uuid__c;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Timestamp timestamp__c;
	
	private String name;
	private String memo__c;
	private Double geolocation__latitude__s;
	private Double geolocation__longitude__s;
	private String address__c;
}
