package bridge.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(request -> request
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
				.permitAll()
			)
			.logout(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
