package bridge.demo.config;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import bridge.demo.dto.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtTokenProvider {

	public static String HttpHeaderInputValue = "Authorization";

	@Value("${JwtKey}")
	private String secretKey;
	private Long tokenValidTime = 60 * 60 * 1000L; //토큰 유효시간 :60분

	private final UserDetailsService userDetailsService;

	public JwtTokenProvider(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@PostConstruct
	protected void SecretKeyEncode() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	//토큰 생성
	public TokenInfo tokenProvide(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		Date now = new Date();

		String accessToken = Jwts.builder()
			.setSubject(authentication.getName()) //Jwt payload에 저장되는 정보 단위, user를 식별하는 값을 넣음.
			.claim("auth", authorities)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();

		String refreshToken = Jwts.builder()
			.setExpiration(new Date(now.getTime() + tokenValidTime * 24))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
		return TokenInfo.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	// jwt 토큰에서 인증 정보 조회
	public Authentication retrieveAuthentication(String accessToken) {
		// 토큰 복호화
		Claims claims = checkUserInfo(accessToken);
		if (claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}
		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get("auth").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.checkUserInfo(accessToken).getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}

	// 토큰의 유효성 + 만료일자 확인
	public boolean tokenCheck(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			System.out.println(claims);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 토큰에서 회원 정보 추출
	private Claims checkUserInfo(String token) {
		try {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
