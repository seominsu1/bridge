package bridge.demo.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bridge.demo.domain.Member;
import bridge.demo.dto.UnregisterResDto;
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
	// private final AuthenticationManagerBuilder authenticationManagerBuilder;
	// private final JwtTokenProvider provider;

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
		List<Member> member = memberRepository.findById(memberId);
		return member.get(0);
	}

	// @Transactional
	// public LoginResDto login(String memberId, String password) {
	// 	// 1. Login ID/PW 를 기반으로 Authentication 객체 생성
	// 	// 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
	// 	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId,
	// 		password);
	//
	// 	// 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
	// 	// authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
	// 	Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
	//
	// 	// 3. 인증 정보를 기반으로 JWT 토큰 생성
	// 	TokenInfo tokenInfo = provider.tokenProvide(authentication);
	//
	// 	return LoginResDto.builder()
	// 		.accessToken(tokenInfo.getAccessToken())
	// 		.tokenType(tokenInfo.getGrantType())
	// 		.memberId(memberId)
	// 		.build();
	// }

	@Transactional
	public UnregisterResDto unregister(Member member) {
		UnregisterResDto resDto = new UnregisterResDto();
		List<Member> memberList = memberRepository.findById(member.getMemberId());
		Member findOne = memberList.get(0);
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
		List<Member> membersId = memberRepository.findById(member.getMemberId());
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
			.build();
		return newMember;
	}
}
