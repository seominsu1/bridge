package bridge.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LoginResponseDto {
	private String accessToken;
	private String tokenType;
	private String memberId;
}
