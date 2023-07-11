package bridge.demo.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import bridge.demo.dto.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider provider;

	public CustomLoginSuccessHandler(JwtTokenProvider provider) {
		this.provider = provider;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		TokenInfo tokenInfo = provider.tokenProvide(authentication);
		// response.setHeader("Authorization", "Bearer " + token.getAccessToken());
		// String url = request.getHeader("Origin") + "/hello";
		RestTemplate restTemplate = new RestTemplate();
		String token = "Bearer " + tokenInfo.getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType("text", "html", utf8);
		headers.setContentType(mediaType);
		headers.set("User-Agent", "mozilla");
		headers.set("Accept-Language", "ko");
		//cookie에 token 넣는 로직
		Cookie cookie = new Cookie("token", URLEncoder.encode(token, "utf-8"));
		cookie.setMaxAge(60 * 60 * 24);
		response.addCookie(cookie);
		response.sendRedirect("/hello");
	}

}
