package bridge.demo.common.entity;

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

	@Column(name = "role")
	private String role;

	@Builder
	public Member(String memberId, String password, String email, String token, String created, String role) {
		this.memberId = memberId;
		this.password = password;
		this.email = email;
		this.token = token;
		this.created = created;
		this.role = role;
	}

	public Member() {
	}
}
