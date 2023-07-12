package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {
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
