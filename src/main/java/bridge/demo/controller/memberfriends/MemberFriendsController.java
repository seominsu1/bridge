package bridge.demo.controller.memberfriends;

import bridge.demo.controller.memberfriends.request.MemberFriendsRequest;
import bridge.demo.controller.memberfriends.response.MemberFriendResponse;
import bridge.demo.controller.memberfriends.response.MemberFriendResponses;
import bridge.demo.controller.memberfriends.response.MemberFriendsResponse;
import bridge.demo.repository.member.Member;
import bridge.demo.repository.member.MemberRepository;
import bridge.demo.repository.memberfriend.MemberFriendsRepository;
import bridge.demo.service.MemberFriendsService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member-friends")
public class MemberFriendsController {

    private MemberFriendsRepository memberFriendsRepository;
    private MemberRepository memberRepository;

    private MemberFriendsService memberFriendsService;

    public MemberFriendsController(MemberFriendsRepository memberFriendsRepository,
        MemberRepository memberRepository, MemberFriendsService memberFriendsService) {
        this.memberFriendsRepository = memberFriendsRepository;
        this.memberRepository = memberRepository;
        this.memberFriendsService = memberFriendsService;
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberFriendResponses> findUser(@PathVariable String memberId) {
        return ResponseEntity.ok(memberResponses(memberFriendsService.findUser(memberId)));
    }

    @PutMapping("/{memberId}/add")
    public ResponseEntity addFriends(@PathVariable String memberId, @RequestBody MemberFriendsRequest request) {
        memberFriendsService.addFriends(memberId, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{memberId}/subtract")
    public ResponseEntity subtractFriends(@PathVariable String memberId, @RequestBody MemberFriendsRequest request) {
        memberFriendsService.subtractFriends(memberId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<MemberFriendsResponse> create(@RequestBody MemberFriendsRequest request) {
        return ResponseEntity.ok(MemberFriendsResponse.of(memberFriendsService.create(request.getMemberId(), request.getFriendIds())));

    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteFriends(@PathVariable String memberId) {
        memberFriendsService.delete(memberId);
        return ResponseEntity.noContent().build();
    }


    private MemberFriendResponses memberResponses(List<Member> members) {
        return MemberFriendResponses.of(
            members.stream()
                .map(friend -> MemberFriendResponse.of(friend))
                .toList(),
            members.size());
    }

    @PostMapping("/test/test")
    public void createMember() {
        memberRepository.save(new Member("1", "test1@example.com", "test1"));
        memberRepository.save(new Member("2", "test2@example.com", "test2"));
        memberRepository.save(new Member("3", "test3@example.com", "test3"));
        memberRepository.save(new Member("4", "test4@example.com", "test4"));
        memberRepository.save(new Member("5", "test5@example.com", "test5"));
    }

}
