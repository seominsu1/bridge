package bridge.demo.repository.memberfriend;

import bridge.demo.repository.member.Member;
import java.util.List;

public interface CustomMemberFriendsRepository {
    List<Member> findFriendsByMemberId(String memberId);
}