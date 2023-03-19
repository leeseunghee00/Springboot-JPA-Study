package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository //컴포넌트 내에서 자동으로 스프링 빈으로 관리하게 된다.
@RequiredArgsConstructor
public class MemberRepository {
    //    @PersistenceContext //스프링이 엔티티메니저를 만들어서 주입해 준다.
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member); //JPA가 멤버를 주입한다.
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // jpql에서는 대상이 테이블이 아니라 엔티티가 된다.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
