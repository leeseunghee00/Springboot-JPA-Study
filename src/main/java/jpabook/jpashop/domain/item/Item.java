package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   //상속관계 매핑 - 한 테이블에 다 때려넣는 방식
@DiscriminatorColumn(name = "dtype")    //하위(실체) 클래스를 구분하기 위한 용도
@Getter
@Setter
public abstract class Item {  //상속관계를 갖는다 -> Item은 상위(추상) 클래스 -> abstract 사용
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
