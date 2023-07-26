package bridge.demo.repository.memberfriend;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MemberFriendsRepository extends MongoRepository<MemberFriends, String>, CustomMemberFriendsRepository {

	Optional<MemberFriends> findById(String id);

	Boolean existsByMemberId(String memberId);

	List<MemberFriends> findAllByMemberIdIn(Set<String> memberIds);
}
