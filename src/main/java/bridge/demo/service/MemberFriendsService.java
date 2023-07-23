package bridge.demo.service;

import bridge.demo.controller.memberfriends.request.MemberFriendsRequest;
import bridge.demo.exception.MemberFriendsAlreadyExistException;
import bridge.demo.exception.MemberFriendsNotFoundException;
import bridge.demo.exception.MemberNotFoundException;
import bridge.demo.repository.member.Member;
import bridge.demo.repository.member.MemberRepository;
import bridge.demo.repository.memberfriend.MemberFriends;
import bridge.demo.repository.memberfriend.MemberFriendsRepository;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class MemberFriendsService {

    private MemberFriendsRepository memberFriendsRepository;

    private MemberRepository memberRepository;

    public MemberFriendsService(MemberFriendsRepository memberFriendsRepository,
        MemberRepository memberRepository) {
        this.memberFriendsRepository = memberFriendsRepository;
        this.memberRepository = memberRepository;
    }

    public MemberFriends create(String memberId, Set<String> friendIds) {
        checkIsValidMemberIdAndFriendIds(memberId, friendIds);
        checkAlreadyExistsMemberFriendsByMemberId(memberId);
        MemberFriends memberFriends = createMemberWithEmptyFriends(memberId);
        addConnectionToMember(memberFriends, friendIds);
        addConnectionToFriends(memberId, friendIds);
        return memberFriends;
    }

    private void checkAlreadyExistsMemberFriendsByMemberId(String memberId) {
        if (memberFriendsRepository.existsByMemberId(memberId)) {
            throw new MemberFriendsAlreadyExistException("사용자 친구 리스트가 이미 존재합니다. id : " + memberId);
        }
    }

    public MemberFriends createMemberWithEmptyFriends(String memberId) {
        return memberFriendsRepository.save(MemberFriends.of(memberId));
    }

    public void addFriends(String memberId, MemberFriendsRequest request) {
        checkIsValidMemberIdAndFriendIds(memberId, request.getFriendIds());
        MemberFriends memberFriends = findById(memberId);
        addConnectionToMember(memberFriends, request.getFriendIds());
        addConnectionToFriends(memberId, request.getFriendIds());
    }

    private void addConnectionToMember(MemberFriends memberFriends, Set<String> friendIds) {
        memberFriends.addFriends(friendIds);
        memberFriendsRepository.save(memberFriends);
    }

    private void addConnectionToFriends(String memberId, Set<String> friendIds) {
        List<MemberFriends> memberFriends = findAllByFriendIds(friendIds);
        memberFriends.forEach(friend -> friend.addFriend(memberId));
        memberFriendsRepository.saveAll(memberFriends);
    }

    public void subtractFriends(String memberId, MemberFriendsRequest request) {
        checkIsValidMemberIdAndFriendIds(memberId, request.getFriendIds());
        MemberFriends memberFriends = findById(memberId);
        deleteConnectionToFriends(memberId, memberFriends.getFriendIds());
        deleteConnectionToMember(memberFriends, request.getFriendIds());
    }

    private void deleteConnectionToMember(MemberFriends memberFriends, Set<String> friendIds) {
        memberFriends.deleteFriends(friendIds);
        memberFriendsRepository.save(memberFriends);
    }

    private void deleteConnectionToFriends(String memberId, Set<String> friendIds) {
        List<MemberFriends> memberFriends = findAllByFriendIds(friendIds);
        memberFriends.forEach(friend -> friend.deleteFriend(memberId));
        memberFriendsRepository.saveAll(memberFriends);
    }


    public List<Member> findUser(@PathVariable String memberId) {
        checkIsValidMember(memberId);
        return memberFriendsRepository.findFriendsByMemberId(memberId);
    }

    public void delete(String memberId) {
        MemberFriends memberFriends = findById(memberId);
        deleteConnectionToFriends(memberId, memberFriends.getFriendIds());
        memberFriendsRepository.delete(memberFriends);
    }

    private void checkIsValidMemberIdAndFriendIds(String memberId, Set<String> friendIds) {
        checkIsValidMember(memberId);
        friendIds.forEach(this::checkIsValidMember);
    }

    private void checkIsValidMember(String memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("사용자를 찾을 수 없습니다. id : " + memberId);
        }
    }

    private MemberFriends findById(String memberId) {
        return memberFriendsRepository.findById(memberId).orElseThrow(
            () -> new MemberFriendsNotFoundException("사용자 친구 리스트를 찾을 수 없습니다. id : " + memberId));
    }

    private List<MemberFriends> findAllByFriendIds(Set<String> friendIds) {
        return memberFriendsRepository.findAllByMemberIdIn(friendIds);
    }

}
