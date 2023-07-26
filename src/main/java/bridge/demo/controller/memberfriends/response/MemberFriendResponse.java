package bridge.demo.controller.memberfriends.response;

import bridge.demo.repository.member.Member;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class MemberFriendResponse {

	private ObjectId id;
	private String memberId;
	private String email;

	private MemberFriendResponse(ObjectId id, String email, String name) {
		this.id = id;
		this.email = email;
		this.memberId = name;
	}

	public static MemberFriendResponse of(Member member) {
		return new MemberFriendResponse(member.getId(), member.getEmail(), member.getMemberId());
	}
}
