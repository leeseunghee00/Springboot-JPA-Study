package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToONE(ManyToOne, OneToOne) => 즉, 컬렉션이 아닌 엔티티를 연결해 주는 코드를 작성한다.
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * 엔티티 직접 노출 ... 애초에 좋지 않다 !
     * - Hibernate5Module 모듈 등록 -> LAZY=null 처리
     * - 양방한 관계 문제 발생 -> 한 쪽에 @JsonIgnore 로 설정
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        // 원하는 속성만 뽑아내기 위함 -> 강제로 LAZY Loading하는 방법이다.
        for (Order order : all) {
            order.getMember().getName();    // Lazy 강제 초기화
            order.getDelivery().getAddress();    // Lazy 강제 초기화
        }

        return all;
    }
}
