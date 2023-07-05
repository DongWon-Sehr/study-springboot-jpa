package jpabook.jpashop.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

/**
 * xToOne (ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> getOrdesrV1() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // force lazy loading
            order.getDelivery().getAddress(); // force lazy loading
        }
        return orders;
    }

}
