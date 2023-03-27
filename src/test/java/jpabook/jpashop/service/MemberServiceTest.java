package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)    //junit 시작할 때 스프링이랑 같이 실행하기 위해서
@SpringBootTest                 //스프링부트를 띄운 상태에서 실행하기 위해서
@Transactional                  //데이터를 변경해야 되기 때문에 롤백이 필요함
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setName("kim");
        System.out.println(member);

        //When
        Long saveId = memberService.join(member);

        //Then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
//    @Test(expected = IllegalStateException.class)   //try-catch 구문을 이렇게 처리할 수 있음
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //When
        memberService.join(member1);
//        memberService.join(member2); //예외가 발생해야 한다.

        //Then
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
//        fail("예외가 발생해야 한다.");
    }
}