package bridge.demo.repository.memberfriend;

import bridge.demo.repository.member.Member;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class MemberFriendsRepositoryImpl implements CustomMemberFriendsRepository {

	private final String ID_FIELD = "_id";
	private final String MEMBER_FRIENDS_COLLECTION = "member_friends";
	private final String MEMBER_COLLECTION = "member";

	private final MongoTemplate mongoTemplate;

	public MemberFriendsRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<Member> findFriendsByMemberId(String memberId) {
		String friendsField = "friends";
		Aggregation aggregation = Aggregation.newAggregation(
			match(ID_FIELD, memberId),
			lookupOperation(MEMBER_COLLECTION, "friendIds", ID_FIELD, friendsField),
			Aggregation.unwind(friendsField), // friends 필드의 배열을 분리하여 문서로 확장
			Aggregation.replaceRoot(friendsField) // friends 문서를 루트로 대체하여 결과로 받음
		);
		return mongoTemplate.aggregate(aggregation, MEMBER_FRIENDS_COLLECTION, Member.class)
			.getMappedResults();
	}

	private LookupOperation lookupOperation(String collection, String localField, String foreignField, String resultField) {
		return LookupOperation.newLookup()
			.from(collection)
			.localField(localField)
			.foreignField(foreignField)
			.as(resultField);
	}

	private MatchOperation match(String field, String expected) {
		Criteria criteria = Criteria.where(field).is(expected);
		return Aggregation.match(criteria);
	}
}
