package bridge.demo.repository.member;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "member")
@Getter
public class Member {

	@Id
	private ObjectId id;

	@NotNull
	@Indexed(unique = true)
	private String memberId;

	@NotNull
	private String password;

	@NotNull
	@Indexed(unique = true)
	private String email;

	private String token;

	private String created;

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
