package com.food.ordering.system.service.domain;

import com.food.ordering.system.domain.exception.DomainException;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Product;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.service.domain.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{

    public static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
         validateRestaurant(restaurant);
         setOrderProductInformation(order,restaurant);
         order.validateOrder();
         order.initializeOrder();
         log.info("Order with ID {} has been initiated ",order.getCustomerId());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getOrderItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct->{
                Product currentProduct = orderItem.getProduct();
                if(currentProduct.equals(restaurantProduct)){
                    currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
                                                                   restaurantProduct.getPrice());
                }
        }));

    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()){
             throw new DomainException("restaurant with id "+ restaurant.getId().getValue() + "is not active");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.paid();
        log.info("Order with ID: {} get paid",order.getId().getValue());
        return new OrderPaidEvent(order,ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveEvent(Order order) {
       order.approve();
       log.info("Order with ID: {} has been approved",order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderEvent(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for id: {}",order.getId().getValue());
        return new OrderCancelledEvent(order,ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
          order.cancel(failureMessages);
          log.info("Order with ID {} has been cancelled",order.getId().getValue());

    }
}
