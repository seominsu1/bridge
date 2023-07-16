package bridge.demo.repository.memberfriend;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "member_friends")
@Getter
public class MemberFriends {

    @Id
    private String memberId;

    private LinkedHashSet<String> friendIds;

    @Override
    public String toString() {
        return "MemberFriends{" +
            "memberId='" + memberId + '\'' +
            ", friendIds=" + friendIds +
            '}';
    }

    private MemberFriends(String memberId, Set<String> friendIds) {
        this.memberId = memberId;
        LinkedHashSet<String> ids = new LinkedHashSet<>();
        friendIds.addAll(friendIds);
        this.friendIds = ids;
    }

    public static MemberFriends of(String memberId, Set<String> friendIds) {
        return new MemberFriends(memberId, friendIds);
    }

    public void addFriend(String friendId) {
        this.friendIds.add(friendId);
    }

    public void deleteFriend(String friendId) {
        this.friendIds.remove(friendId);
    }

    public void addFriends(Set<String> friendIds) {
        System.out.println(this.friendIds);
        this.friendIds.addAll(friendIds);
        System.out.println(friendIds + "this" + this.friendIds);
    }
}
