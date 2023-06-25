package jpabook.jpashop.api;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.service.ItemService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemApiController {
    
    private final ItemService itemservice;

    @PostMapping("/api/v2/items")
    public CreateItemResponse saveItemV2(@RequestBody @Valid CreateItemRequest request) {
        Book book = new Book();
        book.setName(request.getName());
        book.setPrice(request.getPrice());
        book.setStockQuantity(request.getStockQuantity());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());

        Long id = itemservice.saveItem(book);
        return new CreateItemResponse(id);
    }

    @Data
    static class CreateItemRequest {

        @NotEmpty
        private String name;
        private int price;
        private int stockQuantity;
        private String author;
        private String isbn;
    }

    @Data
    static class CreateItemResponse {
        
        private Long id;

        public CreateItemResponse(Long id) {
            this.id = id;
        }
    }
}
