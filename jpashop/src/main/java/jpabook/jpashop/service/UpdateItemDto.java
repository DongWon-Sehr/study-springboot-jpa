package jpabook.jpashop.service;

import jpabook.jpashop.controller.BookForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemDto {
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;

    public UpdateItemDto(Long itemId, BookForm bookform) {
        this.id = itemId;
        this.name = bookform.getName();
        this.price = bookform.getPrice();
        this.stockQuantity = bookform.getStockQuantity();
        // this.author = bookform.getAuthor();
        // this.isbn = bookform.getIsbn();
    }
}
