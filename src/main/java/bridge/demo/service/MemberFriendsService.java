package bridge.demo.service;

import bridge.demo.controller.memberfriends.request.MemberFriendsRequest;
import bridge.demo.exception.MemberFriendsAlreadyExistException;
import bridge.demo.exception.MemberFriendsNotFoundException;
import bridge.demo.exception.MemberNotFoundException;
import bridge.demo.repository.member.Member;
import bridge.demo.repository.member.MemberRepository;
import bridge.demo.repository.memberfriend.MemberFriends;
import bridge.demo.repository.memberfriend.MemberFriendsRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

    public void addFriends(String memberId, MemberFriendsRequest request) {
        checkIsValidMemberIdAndFriendIds(memberId, request.getFriendIds());
        MemberFriends m = memberFriendsRepository.findById(memberId).get();
        System.out.println(m);
        System.out.println(m.getMemberId());
        System.out.println(m.getFriendIds());
        MemberFriends memberFriends = memberFriendsRepository.findById(memberId)
            .orElseGet(() -> createMemberWithEmptyFriends(memberId));
        addConnectionToMember(memberFriends, request.getFriendIds());
        addConnectionToFriends(memberId, request.getFriendIds());
    }

    private void checkIsValidMemberIdAndFriendIds(String memberId, Set<String> friendIds) {
        checkIsValidMember(memberId);
        friendIds.forEach(this::checkIsValidMember);
    }


    public MemberFriends createMemberWithEmptyFriends(String memberId) {
        checkIsValidMember(memberId);
        return memberFriendsRepository.save(MemberFriends.of(memberId, Collections.emptySet()));
    }


    private void checkIsValidMember(String memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("사용자를 찾을 수 없습니다. id : " + memberId);
        }
    }


    private void addConnectionToMember(MemberFriends memberFriends, Set<String> friendIds) {
        memberFriends.addFriends(friendIds);
        memberFriendsRepository.save(memberFriends);
    }

    private void addConnectionToFriends(String memberId, Set<String> friendIds) {
        List<MemberFriends> memberFriends = memberFriendsRepository.findAllByMemberIdIn(friendIds);
        if (!findAllMemberFriends(memberFriends, friendIds)) {
            memberFriends.addAll(createNotFoundMembersWithEmptyFriends(memberFriends, friendIds));
        }
        memberFriends.forEach(friend -> friend.addFriend(memberId));
        memberFriendsRepository.saveAll(memberFriends);
    }

    private boolean findAllMemberFriends(List<MemberFriends> memberFriends, Set<String> friendIds) {
        return memberFriends.size() == friendIds.size();
    }

    private List<MemberFriends> createNotFoundMembersWithEmptyFriends(List<MemberFriends> memberFriends, Set<String> friendIds) {
        List<String> memberIds = memberFriends.stream().map(MemberFriends::getMemberId).toList();
        Set<String> filteredFriendIds = friendIds.stream()
            .filter(id -> !memberIds.contains(id))
            .collect(Collectors.toSet());
        return createMembersWithEmptyFriends(filteredFriendIds);
    }

    public List<MemberFriends> createMembersWithEmptyFriends(Set<String> memberIds) {
        memberIds.forEach(this::checkIsValidMember);
        return memberIds.stream()
            .map(memberId -> MemberFriends.of(memberId, Collections.emptySet()))
            .toList();
    }

    public List<Member> findUser(@PathVariable String memberId) {
        checkIsValidMember(memberId);
        return memberFriendsRepository.findFriendsByMemberId(memberId);
    }


    public void delete(String memberId) {
        MemberFriends memberFriends = memberFriendsRepository.findById(memberId).orElseThrow(
            () -> new MemberFriendsNotFoundException("사용자 친구 리스트를 찾을 수 없습니다. id : " + memberId));
        deleteConnectionToFriends(memberId, memberFriends.getFriendIds());
        memberFriendsRepository.delete(memberFriends);
    }

    private void deleteConnectionToFriends(String memberId, Set<String> friendIds) {
        List<MemberFriends> memberFriends = memberFriendsRepository.findAllByMemberIdIn(friendIds)
            .stream().map(friend -> {
                friend.deleteFriend(memberId);
                return friend;
            }).toList();
        memberFriendsRepository.saveAll(memberFriends);
    }


}
