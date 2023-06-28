package bridge.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bridge.demo.domain.Member;
import bridge.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public Long save(Member member) throws IllegalStateException {
		try {
			validateDuplicatedId(member);
		} catch (IllegalStateException e) {
			log.info(e.getMessage());
			throw e;
		}
		memberRepository.save(member);
		return member.getId();
	}

	public Member findOne(String memberId) {
		List<Member> member = memberRepository.findById(memberId);
		return member.get(0);
	}

	@Transactional
	public Member login(String memberId) {

		return null;
	}

	private void validateDuplicatedId(Member member) {

		List<Member> membersEmail = memberRepository.findByEmail(member.getEmail());
		List<Member> membersId = memberRepository.findById(member.getMemberId());
		if (!membersEmail.isEmpty()) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		} else if (!membersId.isEmpty()) {
			throw new IllegalStateException("중복된 ID 입니다.");
		}
	}
}
