package com.jm.online_store.service.impl;

import com.jm.online_store.exception.OrdersNotFoundException;
import com.jm.online_store.model.Order;
import com.jm.online_store.model.dto.OrderDTO;
import com.jm.online_store.model.dto.SalesReportDto;
import com.jm.online_store.repository.OrderRepository;
import com.jm.online_store.service.interf.OrderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public List<Order> findAllByUserIdAndStatus(Long userId, Order.Status status) {
        return orderRepository.findAllByUserIdAndStatus(userId, status);
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * метод построения OrderDTO из Order, полученного из БД.
     *
     * @param id идентификатор.
     * @return объект OrderDTO
     */
    @Override
    public OrderDTO findOrderDTOById(Long id) {
        return convertOrderToOrderDTO(orderRepository.findById(id).get());
    }

    /**
     * метод конвертации Order в OrderDTO для отсекания лишних данных.
     *
     * @param order объект order.
     * @return объект OrderDTO.
     */
    private OrderDTO convertOrderToOrderDTO(Order order) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(order, OrderDTO.class);
    }

    /**
     * Метод добавления заказа.
     * Первоначально кол-во продуктов и общая стоимость равны 0,
     * эти поля изменяются методом {@link ProductInOrderServiceImpl#addToOrder(long, long, int)}
     *
     * @param order заказ, сохраняемый в базу
     * @return id сохранённого объекта
     */
    @Override
    public Long addOrder(Order order) {
        order.setAmount(Long.valueOf(0));
        order.setOrderPrice(Double.valueOf(0));
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    @Override
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    /**
     * Method finds all orders with status Status.COMPLETED between start and end date
     *
     * @param startDate - {@link LocalDate} beginning of period
     * @param endDate   - {@link LocalDate} end of period
     * @return - returns {@link List<SalesReportDto>}
     */
    @Override
    public List<SalesReportDto> findAllSalesBetween(LocalDate startDate, LocalDate endDate) {
        List<Order> completedOrders = orderRepository.findAllByStatusEqualsAndDateTimeBetween(Order.Status.COMPLETED, startDate.atStartOfDay(), endDate.atTime(23,59,59));
        if (completedOrders.isEmpty()) {
            throw new OrdersNotFoundException("There are no completed orders in custom date range");
        }
        List<SalesReportDto> salesList = new ArrayList<>();
        completedOrders.forEach(order -> salesList.add(SalesReportDto.orderToSalesReportDto(order)));
        return salesList;
    }
}
