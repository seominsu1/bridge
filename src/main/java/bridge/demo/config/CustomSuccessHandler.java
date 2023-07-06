package bridge.demo.config;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import bridge.demo.dto.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider provider;

	public CustomSuccessHandler(JwtTokenProvider provider) {
		this.provider = provider;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		TokenInfo token = provider.tokenProvide(authentication);
		// response.setHeader("Authorization", "Bearer " + token.getAccessToken());
		log.info("token= " + token);
		String url = request.getHeader("Origin") + "/hello";
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token.getAccessToken());
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType("text", "html", utf8);
		headers.setContentType(mediaType);
		headers.set("User-Agent", "mozilla");
		headers.set("Accept-Language", "ko");

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		log.info("responseEntity" + responseEntity.getBody());
	}

}
