package bridge.demo.controller.memberfriends.request;

import com.mongodb.lang.Nullable;
import java.util.Set;
import lombok.Getter;

@Getter
public class MemberFriendsRequest {

	private String memberId;

	private Set<String> friendIds;
}