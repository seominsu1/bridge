package bridge.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import bridge.demo.common.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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

	public Member findById(String memberId) {
		return em.createQuery("select m from Member m where m.memberId = :memberId", Member.class)
			.setParameter("memberId", memberId)
			.getSingleResult();
	}

	public List<Member> findMembers(String memberId) {
		return em.createQuery("select m from Member m where m.memberId = :memberId", Member.class)
			.setParameter("memberId", memberId)
			.getResultList();
	}

	public void unregister(Long id) {

		Query query = em.createQuery("delete from Member m where m.id = :id").setParameter("id", id);
		query.executeUpdate();
	}
}
