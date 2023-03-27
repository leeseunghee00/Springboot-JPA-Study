package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service    //스프링에서 제공하는 어노테이션, 자동으로 스프링 빈으로 역시 등록됨
@Transactional(readOnly = true) //JPA의 어떤 모든 데이터 변경이나 로직들은 Transaction 안에서 동작하도록 해야 한다.
@RequiredArgsConstructor    //final이 있는 필드만 생성자를 만들어 준다.
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional  //default는 false임
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}