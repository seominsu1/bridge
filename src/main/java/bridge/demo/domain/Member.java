package bridge.demo.domain;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "member_id", nullable = false)
	private String memberId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "token")
	private String token;

	@Column(name = "created")
	private String created;

	@Builder
	public Member(String memberId, String password, String email, String token, String created) {
		Assert.hasText(memberId, "memberId must not be empty");
		Assert.hasText(password, "password must not be empty");
		Assert.hasText(email, "email must not be empty");
		this.memberId = memberId;
		this.password = password;
		this.email = email;
		this.token = token;
		this.created = created;
	}

	public Member() {
	}
}
