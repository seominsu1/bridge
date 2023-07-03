package bridge.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import bridge.demo.dto.TokenInfo;
import jakarta.servlet.DispatcherType;
import lombok.extern.log4j.Log4j2;

@Log4j2
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

	JwtTokenProvider provider;
	JwtTokenFilter filter;

	public SpringSecurityConfig(JwtTokenProvider provider, JwtTokenFilter filter) {
		this.provider = provider;
		this.filter = filter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
		requestCache.setMatchingRequestParameterName("continue");
		http.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(request -> request
				.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
				.requestMatchers("/member/save", "/").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(login -> login
				.loginPage("/member/login")    // [A] 커스텀 로그인 페이지 지정
				.loginProcessingUrl("/member/login-post")    // [B] submit 받을 url
				.usernameParameter("memberId")    // [C] submit할 아이디
				.passwordParameter("password")    // [D] submit할 비밀번호
				.defaultSuccessUrl("/hello", true)
				.successHandler((request, response, auth) -> {
					String ip = request.getRemoteAddr();
					String user_id = auth.getName();

					log.info("login ok : " + ip + " " + user_id);

					response.setCharacterEncoding("UTF-8");
					response.setHeader("Content-Type", "application/json; UTF-8");
					TokenInfo token = provider.tokenProvide(auth);
					response.getWriter().write("{\"result\" : \"" + token + "\" }");
					response.sendRedirect("/hello");
					log.info("response : " + response);
				})
				.failureHandler((request, response, auth) -> {
					String ip = request.getRemoteAddr();
					String user_id = request.getParameter("username");

					log.info("login fail : " + ip + " " + user_id);

					response.sendRedirect("/");
				})
				.permitAll()
			)
			.logout(Customizer.withDefaults());

		return http.build();
	}

}
