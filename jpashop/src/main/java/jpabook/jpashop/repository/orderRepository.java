package jpabook.jpashop.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;

import static jpabook.jpashop.domain.QMember.*;
import static jpabook.jpashop.domain.QOrder.*;

@Repository
// @RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;
    
    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        return query
            .select(order)
            .from(order)
            .join(order.member, member)
            .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
            .limit(1000)
            .fetch();
    }

    private BooleanExpression nameLike(String memberName) {
        if (StringUtils.hasText(memberName)) {
            return null;
        }
        return member.name.like(memberName);
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return order.status.eq(statusCond);
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // search order status
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // search meember name
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(criteria.toArray(new Predicate[criteria.size()]));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        // fetch join to avoid lazy loading
        return em.createQuery(
            "select o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<Order> findAllByItem() {
        // collection fetch join to avoid lazy loading with distinct - remove duplicate of root entity (Order)
        // cons-1: can't use paging
        // cons-2: can only 1 collection fetch join
        return em.createQuery(
            "select distinct o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch oi.item i", Order.class
        ).getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        // fetch join to avoid lazy loading
        return em.createQuery(
            "select o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d", Order.class)
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
    }

}
