package bridge.demo.repository;

import java.util.List;

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

	public Member findOne(Long id) {
		return em.find(Member.class, id);
	}

	public List<Member> findAll(Long id) {
		return em.createQuery("select m from Member m", Member.class).getResultList();
	}

	public List<Member> findByEmail(String email) {
		return em.createQuery("select m from Member m where m.email = :email", Member.class)
			.setParameter("email", email)
			.getResultList();
	}

	public List<Member> findById(String memberId) {
		return em.createQuery("select m from Member m where m.memberId = :memberId", Member.class)
			.setParameter("memberId", memberId)
			.getResultList();
	}
}
