package com.example.pet.repository;

import com.example.pet.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.member.identity = :identity order by o.orderDate desc")
    public List<Order> findOrders(String identity, Pageable pageable);

    @Query("select count(o) from Order o where o.member.identity = :identity")
    public Long totalCount(String identity);

}
