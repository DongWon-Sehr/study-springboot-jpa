package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {
    
    private Long id;

    // item common field
    private String name;
    private int price;
    private int stockQuantity;

    // book field
    private String author;
    private String isbn;
}
