package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id") //PK 정의 - @Column(name = PK)
    private Long id;

    //    @NotEmpty   //null값 허용 X -> api에 따라서 엔티티 설정이 다를 수 있기 때문에 이러한 설정은 dto에서 별도로 설정해 주는 것이 좋다.
    private String name;

    @Embedded   //내장타입을 포함했다는 것을 말함
    private Address address;

    @OneToMany(mappedBy = "member")  //일대다 관계, mappedBy는 Order 테이블에 있는 member에 매핑된 것을 의미한다.(즉, 주인이 아니라는 뜻 )
    private List<Order> orders = new ArrayList<>();
}
