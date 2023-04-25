package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 * userA
 * - JPA1 BOOK
 * - JPA2 BOOK
 * userB
 * - SPRING1 BOOK
 * - SPRING2 BOOK
 */
@Component  //spring이 component 대싱이 된다.
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    // spring life-cycle이 있기 때문에 Transaction 하는 것이 어렵다.
    // 따라서 dbInit1()의 코드를 한번에 넣는 것이 아니라
    // 아래와 같이 init으로 따로 빼서 별도의 빈으로 넣어준다.
    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("Seoul", "1", "1234"));
            em.persist(member);

            Book book1 = new Book();
            book1.setName("JPA1 BOOK");
            book1.setPrice(10000);
            book1.setStockQuantity(100);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA2 BOOK");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            //OrderItem... 로 선언했기 때문에 주문상품이 배열로 넘어감
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
    }
}

