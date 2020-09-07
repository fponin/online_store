package com.jm.online_store.service.interf;

import com.jm.online_store.model.Order;
import com.jm.online_store.model.dto.OrderDTO;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> findAll();

    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByUserIdAndStatus(Long userId, Order.Status status);

    Optional<Order> findOrderById(Long id);

    Long addOrder(Order order);

    void updateOrder(Order order);

    OrderDTO findOrderDTOById(Long id);
}
