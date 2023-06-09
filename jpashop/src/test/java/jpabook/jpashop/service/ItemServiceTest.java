package jpabook.jpashop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.controller.BookForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
    
    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired EntityManager em;

    @Test
    public void saveTest() throws Exception {
        // given
        Book book = new Book();
        book.setName("The Little Prince");
        book.setPrice(15000);
        book.setStockQuantity(15);
        book.setAuthor("Saint-Exupéry");
        book.setIsbn("1234");

        // when
        itemService.saveItem(book);

        // then
        assertEquals(book, itemService.findOne(book.getId()));
    }

    @Test
    public void updateTestWithUpdateItem() throws Exception {
        // given
        Book book = new Book();
        book.setName("The Little Prince");
        book.setPrice(15000);
        book.setStockQuantity(15);
        book.setAuthor("Saint-Exupéry");
        book.setIsbn("1234");
        itemService.saveItem(book);

        String updateName = "The Middle Prince";
        int updatePrice = 10000;
        int updateStockQuantity = 10;

        BookForm bookForm = new BookForm();
        bookForm.setId(book.getId());
        bookForm.setName(updateName);
        bookForm.setPrice(updatePrice);
        bookForm.setStockQuantity(updateStockQuantity);

        UpdateItemDto updateBookDto = new UpdateItemDto(book.getId(), bookForm);

        // when
        itemService.updateItem(updateBookDto);
        
        // then
        assertEquals(updateName, itemService.findOne(book.getId()).getName());
        assertEquals(updatePrice, itemService.findOne(book.getId()).getPrice());
        assertEquals(updateStockQuantity, itemService.findOne(book.getId()).getStockQuantity());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void updateTestWithSaveItem() throws Exception {
        // given
        Book book = new Book();
        book.setName("The Little Prince");
        book.setPrice(15000);
        book.setStockQuantity(15);
        book.setAuthor("Saint-Exupéry");
        book.setIsbn("1234");
        itemService.saveItem(book);

        String updateName = "The Middle Prince";
        int updatePrice = 10000;
        int updateStockQuantity = 10;

        Book updateBook = new Book();
        updateBook.setId(book.getId());
        updateBook.setName(updateName);
        updateBook.setPrice(updatePrice);
        updateBook.setStockQuantity(updateStockQuantity);
        updateBook.setAuthor(book.getAuthor());
        updateBook.setIsbn(book.getIsbn());

        // when
        itemService.saveItem(updateBook);
        
        // then
        fail("InvalidDataAccessApiUsageException should be erased");
    }
}
