package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    // response entity object and force lazy loading
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

    // response DTO object
    @GetMapping("/api/v2/orders")
    public List<OrderDto> getOrdersV2() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        return orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    // response DTO object with fetch join to avoid lazy loading (can't support paging)
    @GetMapping("/api/v3/orders")
    public List<OrderDto> getOrdersV3() {
        return orderRepository.findAllByItem().stream()
            .map(OrderDto::new)
            .collect(Collectors.toList());
    }

    // response DTO object with fetch join ToOne relation entity to avoid lazy loading 
    // and get collection with lazy loading (use hibernate.default_batch_fetch_size, @BatchSize)
    // (recommend batch_size: 100 ~ 1000)
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> getOrdersV3_page(
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        return orderRepository.findAllWithMemberDelivery(offset, limit).stream()
            .map(OrderDto::new)
            .collect(Collectors.toList());
    }

    // response DTO object with fetch join that find DTO method
    // slightly better performance than v3 but less reusability
    // query 1+N
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> getOrdersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    // qeury optimized version of V4
    // query 1+1
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> getOrdersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @Getter
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {
        private Long orderItemId;
        private String itemName;
        private int orderPrice;
        private int orderCount;
        private int totalPrice;

        public OrderItemDto(OrderItem orderItem) {
            this.orderItemId = orderItem.getId();
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.orderCount = orderItem.getCount();
            this.totalPrice = orderItem.getTotalPrice();
        }
    }
}
