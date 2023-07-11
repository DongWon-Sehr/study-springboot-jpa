package jpabook.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    
    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> orders = findOrders();
        
        List<Long> orderIds = getOrderIds(orders);
        
        Map<Long, List<OrderItemQueryDto>> orderItemsMap = findOrderItemsMap(orderIds);
        
        orders.forEach(o -> o.setOrderItems(orderItemsMap.get(o.getOrderId())));

        return orders;
    }

    public List<OrderQueryFlatDto> findAllByDto_flat() {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderQueryFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
            " from Order o" +
            " join o.member m" +
            " join o.delivery d" +
            " join o.orderItems oi" +
            " join oi.item i", OrderQueryFlatDto.class).getResultList();
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemsMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems =  em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
            " from OrderItem oi" +
            " join oi.item i" +
            " where oi.order.id in :orderIds", OrderItemQueryDto.class)
            .setParameter("orderIds", orderIds)
            .getResultList();
        
        Map<Long, List<OrderItemQueryDto>> orderItemsMap = orderItems.stream()
            .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemsMap;
    }

    private List<Long> getOrderIds(List<OrderQueryDto> orders) {
        return orders.stream()
            .map(o->o.getOrderId())
            .collect(Collectors.toList());
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
            " from OrderItem oi" +
            " join oi.item i" +
            " where oi.order.id = :orderId", OrderItemQueryDto.class)
            .setParameter("orderId", orderId)
            .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
            " from Order o" +
            " join o.member m" +
            " join o.delivery d", OrderQueryDto.class)
            .getResultList();
    }
}
