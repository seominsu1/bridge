package bridge.demo.controller.memberfriends.response;

import java.util.List;
import lombok.Getter;

@Getter
public class MemberFriendResponses {

    private List<MemberFriendResponse> friends;
    private Integer totalElement;

    private MemberFriendResponses(List<MemberFriendResponse> friends, Integer totalElement) {
        this.friends = friends;
        this.totalElement = totalElement;
    }

    public static MemberFriendResponses of(List<MemberFriendResponse> friends, Integer totalElement) {
        return new MemberFriendResponses(friends, totalElement);
    }
}
