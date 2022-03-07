package araobp.nominatim;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@Component
public class NominatimImpl implements Nominatim {
	
	static final String BASE_URL = "https://nominatim.openstreetmap.org";
	
	WebClient client = WebClient.builder()
									.baseUrl(BASE_URL)
									.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
									.defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "ja")  // TODO: Get locale
									.build();
			
	public String getAddress(Double latitude, Double longitude) {
		
		ResponseSpec res = client.get()
		  .uri(uriBuilder -> uriBuilder
		    .path("/reverse")
		    .queryParam("lat", latitude.toString())
		    .queryParam("lon", longitude.toString())
		    .queryParam("format", "json")
		    .build())
		  .retrieve();
		ReverseGeocode reverseGeocode = res.bodyToMono(ReverseGeocode.class).block();
		String displayName = reverseGeocode.getDisplay_name();
	    List<String> list = Arrays.asList(displayName.split(","));
	    list = list.stream().map(String::trim).collect(Collectors.toList());
	    Collections.reverse(list);
	    return String.join(" ", list.subList(2, list.size()));
	}
	
}
