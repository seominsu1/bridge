package bridge.demo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtTokenFilter extends GenericFilterBean {

	private JwtTokenProvider provider;

	public JwtTokenFilter(JwtTokenProvider provider) {
		this.provider = provider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		String token = resolveToken((HttpServletRequest)request);
		//유효한 토큰인지 확인
		if (token != null && provider.tokenCheck(token)) {
			// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
			Authentication authentication = provider.retrieveAuthentication(token);
			//SecurityContext 에 Authentication 객체를 저장합니다.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("jwt token authentication = {}", authentication);

		}
		chain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(provider.HttpHeaderInputValue);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
