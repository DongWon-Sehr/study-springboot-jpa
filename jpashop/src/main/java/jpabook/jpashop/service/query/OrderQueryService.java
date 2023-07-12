package jpabook.jpashop.service.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {
    
    private final OrderRepository orderRepository;

    public List<OrderDto> getOrdersV3() {
        return orderRepository.findAllByItem().stream()
            .map(OrderDto::new)
            .collect(Collectors.toList());
    }
}
