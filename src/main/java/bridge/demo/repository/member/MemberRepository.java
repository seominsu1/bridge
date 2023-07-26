package bridge.demo.repository.member;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, ObjectId> {

	Optional<Member> findByEmail(String email);

	Optional<Member> findByMemberId(String memberId);

	Boolean existsByEmail(String email);

	Boolean existsByMemberId(String memberId);
}
