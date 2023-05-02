package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 엔티티가 아닌 특정 화면과 의존된(관련이 있는) 것들을 작성한다.
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /**
     * 컬렉션은 별도로 조회
     * Query: 루트 1번, 컬렉션 N번 ... 하지만 결과적으로는 N+1 문제가 발생한다.
     * 단건 조회에서 많이 사용하는 방식
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        // 루트 조회; ToOne 코드를 모두 한번에 조회한다.
        List<OrderQueryDto> result = findOrders();  // query 1번 -> N개

        // 루프를 돌면서 컬렉션 부분을 직접 채운다.
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());   // query N번
            o.setOrderItems(orderItems);
        });

        return result;
    }

    /**
     * 1:N 관계(컬렉션)을 제외한 나머지를 한번에 조회
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    /**
     * 1:N 관계인 orderItems 조회
     */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    /**
     * 루프 없이 한방에 가져오기
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        // 루트 조회 - id를 List로 받아서
        List<Long> orderIds = toOrderIds(result);

        // 아래와 같이 orderItem 컬렉션을 MAP 한방에 처리(= 조회한다.)
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        // 루프를 톨면서 컬렉션 추가 - 추가 쿼리 실행 X
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // 메모리에 Map을 올려두고, 찾아서 뽑는다.
        // -> 이렇게 하면 query 2번으로 최적화할 수 있다.
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(Collectors.toList());
    }

    /**
     * DB 에서 쿼리를 한방에 가져오기
     */
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderItems oi" +
                                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
