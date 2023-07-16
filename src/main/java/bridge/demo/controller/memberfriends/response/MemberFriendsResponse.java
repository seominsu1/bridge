package bridge.demo.controller.memberfriends.response;


import bridge.demo.repository.memberfriend.MemberFriends;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
public class MemberFriendsResponse {

    private String memberId;

    private Set<String> friendIds;

    public MemberFriendsResponse(String memberId, Set<String> friendIds) {
        this.memberId = memberId;
        this.friendIds = friendIds;
    }

    public static MemberFriendsResponse of(MemberFriends memberFriends) {
        return new MemberFriendsResponse(memberFriends.getMemberId(), memberFriends.getFriendIds());
    }
}
