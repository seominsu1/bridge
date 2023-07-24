package bridge.demo.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bridge.demo.domain.Member;
import bridge.demo.dto.UnregisterResponseDto;
import bridge.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Transactional
	public void save(Member member) throws IllegalStateException {
		try {
			validateDuplicatedId(member);
		} catch (IllegalStateException e) {
			log.info(e.getMessage());
			throw e;
		}
		Member newMember = passwordEncode(member);
		memberRepository.save(newMember);
	}

	public Member findOne(String memberId) {
		Member member = memberRepository.findById(memberId);
		return member;
	}

	@Transactional
	public UnregisterResponseDto unregister(Member member) {
		UnregisterResponseDto resDto = new UnregisterResponseDto();
		Member findOne = memberRepository.findById(member.getMemberId());
		if (passwordEncoder.matches(member.getPassword(), findOne.getPassword())) {
			memberRepository.unregister(findOne.getId());
			resDto.setMessage("회원탈퇴를 성공적으로 처리했습니다.");
			resDto.setStatus(200);
		} else {
			resDto.setMessage("비밀번호가 일치하지 않습니다.");
			resDto.setStatus(400);
		}
		return resDto;
	}

	private void validateDuplicatedId(Member member) {

		List<Member> membersEmail = memberRepository.findByEmail(member.getEmail());
		List<Member> membersId = memberRepository.findMembers(member.getMemberId());
		if (!membersEmail.isEmpty()) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		} else if (!membersId.isEmpty()) {
			throw new IllegalStateException("중복된 ID 입니다.");
		}
	}

	private Member passwordEncode(Member member) {
		String encodedPw = passwordEncoder.encode(member.getPassword());
		Member newMember = new Member().builder()
			.memberId(member.getMemberId())
			.password(encodedPw)
			.email(member.getEmail())
			.created(member.getCreated())
			.role(member.getRole())
			.build();
		return newMember;
	}
}
