package bridge.demo.repository;

import org.springframework.stereotype.Repository;

import bridge.demo.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	private final EntityManager em;

	public void save(Member member) {
		em.persist(member);
	}
}
