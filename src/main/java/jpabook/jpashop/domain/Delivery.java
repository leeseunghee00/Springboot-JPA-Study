package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)   //.STRING = 중간에 값이 추가되더라도 에러없이 들어갈 수 있다. (꼭 STRING을 쓰자 !)
    private DeliveryStatus status;  //ENUM [READY(준비), COMP(배송)]
}
