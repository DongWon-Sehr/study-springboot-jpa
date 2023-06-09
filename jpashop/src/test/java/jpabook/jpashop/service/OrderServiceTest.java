package jpabook.jpashop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void orderItemTest() throws Exception {
        // given
        Member member = createMember();

        String itemName = "The Little Prince";
        int itemPrice = 15000;
        int itemStockQuantity = 30;
        Book book = createBook(itemName, itemPrice, itemStockQuantity);

        int orderCount = 3;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("Order status should be ORDER, when order item", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("Order count should be exact", orderCount, getOrder.getOrderItems().stream().mapToInt(OrderItem::getCount).sum());
        assertEquals("Total Price = orderCount * price", itemPrice * orderCount, getOrder.getTotalPrice());
        assertEquals("Stock should be reflected as much as order count", itemStockQuantity - orderCount, book.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void orderItemOutOfStockExceptionTest() throws Exception {
        // given
        Member member = createMember();

        String itemName = "The Little Prince";
        int itemPrice = 15000;
        int itemStockQuantity = 10;
        Book book = createBook(itemName, itemPrice, itemStockQuantity);

        int orderCount = 11;

        // when
        orderService.order(member.getId(), book.getId(), orderCount);

        // then
        fail("NotEnoughStockException should be erased");
    }

    @Test
    public void cancelOrderTest() throws Exception {
        // given
        Member member = createMember();

        String itemName = "The Little Prince";
        int itemPrice = 15000;
        int itemStockQuantity = 10;
        Book book = createBook(itemName, itemPrice, itemStockQuantity);

        int orderCount = 3;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("Order status should be CANCEL, when cancel order", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("Stock should be same with initial stock quantity", itemStockQuantity, book.getStockQuantity());

    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("Seoul", "Yeongdong-daero", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}
