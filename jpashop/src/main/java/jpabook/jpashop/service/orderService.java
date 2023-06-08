package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    
    // order
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        
        // find entities
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // create delivery info
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // use member address for implement, but need another address for production

        // create orderItem
        OrderItem orderitem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // create order
        Order order = Order.creatOrder(member, delivery, orderitem);

        // save order
        orderRepository.save(order); // due to cascade option @ Order Class, persist Order, OrderItem, Delivery entities at once

        return order.getId();
    }

    // cancel
    @Transactional
    public void cancelOrder(Long orderId) {
        
        // find order entity
        Order order = orderRepository.findOne(orderId);

        // cancel order
        order.cancel();
    }

    // // search
    // public List<Order> findOrder(OrderSearch orderSearch) {
    //     return orderRepository.findAll(orderSearch);
    // }
}
