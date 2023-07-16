package bridge.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.DispatcherType;
import lombok.extern.log4j.Log4j2;

@Log4j2
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

	private final JwtTokenProvider provider;
	private final JwtTokenFilter filter;

	private final CustomLoginSuccessHandler customLoginSuccessHandler;

	private final CustomLogoutHandler customLogoutHandler;

	private static final String[] PERMIT_URL = {
		"/member/save",
		"/",
		"/member/logout",
		"/member/unregister",
		/*
		* swagger
		* */
		"/swagger-ui.html",
		"/swagger-ui/**",
		"/api/**",
		"/v3/api-docs/**",
		"/v2/api-docs/**",
		"/swagger-resources/**"
	};

	public SpringSecurityConfig(JwtTokenProvider provider, JwtTokenFilter filter,
		CustomLoginSuccessHandler customLoginSuccessHandler, CustomLogoutHandler customLogoutHandler) {
		this.provider = provider;
		this.filter = filter;
		this.customLoginSuccessHandler = customLoginSuccessHandler;
		this.customLogoutHandler = customLogoutHandler;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(
		// 	new ClearSiteDataHeaderWriter(SOURCE));
		http.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(request -> request
				.dispatcherTypeMatchers(DispatcherType.FORWARD)
				.permitAll()
				.requestMatchers(PERMIT_URL)
				.permitAll()
				.requestMatchers("/hello", "/member/unregister", "/member/logout")
				.hasAnyRole("USER", "ADMIN")
				.anyRequest()
				.authenticated()
			)
			.formLogin(login -> login
				.loginPage("/member/login")
				.loginProcessingUrl("/login-post")
				.usernameParameter("memberId")
				.passwordParameter("password")
				.successHandler(customLoginSuccessHandler)
				// .successHandler((request, response, auth) -> {
				// 	String ip = request.getRemoteAddr();
				// 	String user_id = auth.getName();
				//
				// 	log.info("login ok : " + ip + " " + user_id);
				//
				// 	response.setCharacterEncoding("UTF-8");
				// 	response.setHeader("Content-Type", "application/json; UTF-8");
				// 	TokenInfo token = provider.tokenProvide(auth);
				// 	response.setHeader("Authorization", "Bearer " + token.getAccessToken());
				//
				// 	log.info("authorization : " + response.getHeader("Authorization"));
				//
				// })
				.failureHandler((request, response, auth) -> {
					String ip = request.getRemoteAddr();
					String userId = request.getParameter("username");

					log.info("login fail : " + ip + " " + userId);

					response.sendRedirect("/");
				})
				.permitAll()
			)
			.logout((logout) ->
				logout
					.logoutUrl("/member/logout")
					.logoutSuccessUrl("/member/login")
					.addLogoutHandler(customLogoutHandler)
					.logoutSuccessHandler((request, response, authentication) -> response.sendRedirect("/member/login"))
			);
		return http.build();
	}

}
