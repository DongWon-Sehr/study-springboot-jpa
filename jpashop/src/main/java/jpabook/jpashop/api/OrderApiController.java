package jpabook.jpashop.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> getOrdersV1() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

        for (Order order : orders) {
            order.getMember().getName(); // force initialize Lazy Loading
            order.getDelivery().getAddress(); // force initialize Lazy Loading

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName()); // force initialize Lazy Loading
        }

        return orders;
    }
}
