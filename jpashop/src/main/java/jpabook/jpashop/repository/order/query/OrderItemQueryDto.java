package jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class OrderItemQueryDto {

    @JsonIgnore
    private Long orderId;
    private String orderItemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String orderItemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.orderItemName = orderItemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
