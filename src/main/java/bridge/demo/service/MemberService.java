package bridge.demo.service;

import bridge.demo.repository.member.MemberRepository;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bridge.demo.repository.member.Member;
import bridge.demo.dto.UnregisterResponseDto;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional(readOnly = true)
@Log4j2
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void save(Member member) throws IllegalStateException {
		checkIsValidMemberRequest(member);
		Member newMember = passwordEncode(member);
		memberRepository.save(newMember);
	}

	private void checkIsValidMemberRequest(Member member) {
		checkIsValidEmail(member.getEmail());
		checkIsValidMemberId(member.getMemberId());
	}

	private void checkIsValidEmail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new IllegalStateException("중복된 이메일 입니다.");
		}
	}

	private void checkIsValidMemberId(String memberId) {
		if (memberRepository.existsByMemberId(memberId)) {
			throw new IllegalStateException("중복된 ID 입니다.");
		}
	}

	@Transactional
	public UnregisterResponseDto unregister(Member request) {
		UnregisterResponseDto resDto = new UnregisterResponseDto();
		Member member = findByMemberId(request.getMemberId());
		if (passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			memberRepository.deleteById(member.getId());
			resDto.setMessage("회원탈퇴를 성공적으로 처리했습니다.");
			resDto.setStatus(200);
		} else {
			resDto.setMessage("비밀번호가 일치하지 않습니다.");
			resDto.setStatus(400);
		}
		return resDto;
	}


	public Member findById(ObjectId id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("member not found exception."));
		return member;
	}

	public Member findByMemberId(String memberId) {
		Member member = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new RuntimeException("member not found exception."));
		return member;
	}

	public Member findByEmail(String email) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("member not found exception."));
		return member;
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
