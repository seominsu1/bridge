package bridge.demo.repository.member;

import bridge.demo.repository.memberfriend.MemberFriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberFriendsRepository memberFriendsRepository;

}