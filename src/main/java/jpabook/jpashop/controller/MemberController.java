package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new") //홈 화면을 열어보고
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); //memberForm이라는 빈 껍데기를 가져간다.
        return "members/createMemberForm";
    }

    @PostMapping("/members/new") //실제 데이터를 등록하는 목적
    public String create(@Valid MemberForm form, BindingResult result) {
        //@Valid 는 Form에 있는 어노테이션을 쓸 수 있게 해준다.
        //BindingResult 는 오류가 생기면 그걸 담아서 아래 코드들이 실행되게 해준다.
        //Vaild를 통해 from에 있는 어노테이션(NotEmpty)을 실행한다. ex) 이름을 안 쓰면 -> 메시지 출력

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";    //첫 화면(즉, 여기선 홈화면) 으로 넘어가게 된다.
    }

    @GetMapping("/members")
    public String list(Model model) {
        //사실은 Member를 그대로 쓰는 것보다 dto로 쓰는 것이 더 좋은 방법이다.
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
