package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController //@Controller와 @ResponseBody를 합친 어노테이션: RestApi 스타일로 쓰겠다 ! -> 데이터를 Json형태로 바로 보낸다
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    /**
     * 회원 등록 API -> 지양해야 할 코드
     */
    //@RequestBody = Json 데이터를 Member로 매핑해서 넣어준다. (= 바꿔준다)
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 등록 API -> 지향해야 할 코드
     * API 스펙이 바뀌지 않는다 -ex) 값이 변환하면 미리 에러를 알려줌
     * dto로 받으면 api 스펙을 바로 알 수 있다.
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.name);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 회원 등록 API DTO
    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    /**
     * 회원 조회 API -> 지양해야 할 코드
     * 엔티티를 직접 반환하면 안된다 !
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    /**
     * 회원 조회 API -> 지향해야 할 코드
     * api 응답 스펙에 맞춰서 별도의 dto를 반환한다 !
     */
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findmembers = memberService.findMembers();

        // List<Member>를 List<MemberDto>로 바뀌게 된다 !
        List<MemberDto> collect = findmembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    // 회원 조회 API DTO
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    /**
     * 회원 수정
     *
     * @PathVariable 로 수정할 id 값을 받고
     * 수정용 dto -> UpdateMemberRequest
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberResponse(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    // 회원 수정 API DTO
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    // 엔티티에는 lombok 쓰는 것을 자제한다. .. getter 정도만 씀
    // dto에는 막 씀
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}