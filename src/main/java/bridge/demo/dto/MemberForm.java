package bridge.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

	@NotEmpty(message = "Id는 필수입니다.")
	private String memberId;

	@NotEmpty(message = "패스워드는 필수입니다.")
	private String password;

	@NotEmpty(message = "email는 필수입니다.")
	private String email;
}
