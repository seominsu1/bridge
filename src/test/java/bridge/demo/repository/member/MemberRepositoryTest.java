package bridge.demo.repository.member;

import static org.junit.jupiter.api.Assertions.*;

import bridge.demo.repository.memberfriend.MemberFriends;
import bridge.demo.repository.memberfriend.MemberFriendsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberFriendsRepository memberFriendsRepository;

    @Test
    void test() {
        MemberFriends memberFriends = memberFriendsRepository.findById("1").get();
        System.out.println(memberFriends);
    }

}