package jpabook.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDBDummyData {
    
    private final InitService initservice;

    @PostConstruct
    public void Init() {
        initservice.dbInit1();
        initservice.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        
        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("튜나", "서울", "밤거리", "17171771");
            em.persist(member);

            Book book1 = createBook("어린왕자", 11800, 15, "앙투안 드 생텍쥐페리", "9788932917245");
            em.persist(book1);

            Book book2 = createBook("세네카의 인생론", 12000, 12, "루키우스 안나이우스 세네카", "9791160022353");
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 11800, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 12000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.creatOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("지지", "분당", "낮거리", "486");
            em.persist(member);

            Book book1 = createBook("그리스인 조르바", 14800, 20, "니코스 카잔차키스", "9788932909349");
            em.persist(book1);

            Book book2 = createBook("시선으로부터", 14000, 25, "정세랑", "9788954672214");
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 8000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 14000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.creatOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book createBook(String name, int price, int StockQuantity, String author, String isbn) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(StockQuantity);
            book.setAuthor(author);
            book.setIsbn(isbn);
            return book;
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
    }
}
