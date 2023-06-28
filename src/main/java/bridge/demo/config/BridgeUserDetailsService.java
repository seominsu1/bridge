package bridge.demo.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import bridge.demo.domain.Member;
import bridge.demo.service.MemberService;

@Component
public class BridgeUserDetailsService implements UserDetailsService {

	private final MemberService memberService;

	public BridgeUserDetailsService(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public UserDetails loadUserByUsername(String insertedUserId) throws UsernameNotFoundException {
		Optional<Member> findOne = Optional.ofNullable(memberService.findOne(insertedUserId));
		Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

		return User.builder()
			.username(member.getMemberId())
			.password(member.getPassword())
			.build();
	}
}
