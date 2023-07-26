package bridge.demo.config;

import bridge.demo.repository.member.MemberRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import bridge.demo.repository.member.Member;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository memberRepository;

	public UserDetailsServiceImpl(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;

	}

	@Override
	public UserDetails loadUserByUsername(String insertedUserId) throws UsernameNotFoundException {
		Member member = memberRepository.findByMemberId(insertedUserId).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
		return User.builder()
			.username(member.getMemberId())
			.password(member.getPassword())
			.roles(member.getRole())
			.build();
	}
}
