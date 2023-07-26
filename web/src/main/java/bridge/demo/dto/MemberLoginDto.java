package bridge.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberLoginDto {

	@NotEmpty(message = "Id는 필수입니다.")
	private String memberId;

	@NotEmpty(message = "패스워드는 필수입니다.")
	private String password;
}
