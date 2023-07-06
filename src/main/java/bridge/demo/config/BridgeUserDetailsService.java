package bridge.demo.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import bridge.demo.domain.Member;
import bridge.demo.repository.MemberRepository;

@Component
public class BridgeUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	public BridgeUserDetailsService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;

	}

	@Override
	public UserDetails loadUserByUsername(String insertedUserId) throws UsernameNotFoundException {
		Optional<Member> findOne = Optional.ofNullable(memberRepository.findById(insertedUserId));
		Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

		return User.builder()
			.username(member.getMemberId())
			.password(member.getPassword())
			.roles(member.getRole())
			.build();
	}
}
