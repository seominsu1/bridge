package bridge.demo.controller.memberfriends.response;

import bridge.demo.repository.member.Member;
import lombok.Getter;

@Getter
public class MemberFriendResponse {

    private String id;
    private String email;
    private String name;

    private MemberFriendResponse(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static MemberFriendResponse of(Member member) {
        return new MemberFriendResponse(member.getMemberId(), member.getEmail(), member.getName());
    }
}
