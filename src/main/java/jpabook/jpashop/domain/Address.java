package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //어딘가에 내장될 수 있음을 말한다.
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {   //JPA 스펙 상 구현한 생성자
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
