package com.food.ordering.system.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobjects.*;
import com.food.ordering.system.service.domain.exception.OrderDomainException;
import com.food.ordering.system.service.domain.objectvalue.OrderItemId;
import com.food.ordering.system.service.domain.objectvalue.StreetAddress;
import com.food.ordering.system.service.domain.objectvalue.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {


    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> orderItems;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {
        long orderId =1;
           for (OrderItem orderItem:orderItems)  {
               orderItem.initializeOrderItem(super.getId(),new OrderItemId(orderId++));
           }
    }
    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }



    private void validateInitialOrder() {
        if(orderStatus!=null && getId()!=null){
            throw new OrderDomainException("Order is not in the correct state from initialization");
        }
    }
    private void validateTotalPrice() {
        if(price!=null && price.isGreaterThanZero()){
             throw new OrderDomainException("Total price must be greater then Zero!");
        }
    }
    private void validateItemsPrice(){

        Money orderItemsTotal = orderItems.stream().map(orderItem -> {
                 validateItemPrice(orderItem);
                 return orderItem.getSubTotals();
             }).reduce(Money.zero, Money::add);

         if(!price.equals(orderItemsTotal)){
             throw new OrderDomainException("Total price : "+price.getAmount() +
                                            "is not equal to : "+orderItemsTotal + "!");
         }

    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValidate()){
            throw new OrderDomainException("Order item price: "+ orderItem.getPrice().getAmount() +
                    "is valid from product: "+orderItem.getProduct().getId().getValue() );
        }
    }
    public void approve(){
        if(orderStatus!=OrderStatus.PENDING){
            throw new OrderDomainException("Order is not in the correct state for pay operation!");
        }
        orderStatus=OrderStatus.PAID;
    }
    public void paid(){
        if(orderStatus!=OrderStatus.PAID){
            throw new OrderDomainException("Order is not in the correct state for approve operation!");
        }
        orderStatus=OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages){
        if(orderStatus!=OrderStatus.PAID){
            throw new OrderDomainException("Order is not in the correct state for initCancel operation!");
        }
        orderStatus= OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }
    public  void cancel(List<String> failureMessages){
        if(!(orderStatus==OrderStatus.CANCELLING || orderStatus==OrderStatus.PENDING)){
            throw new OrderDomainException("Order is not in the correct state for cancel operation!");
        }
        orderStatus=OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);

    }

    private void updateFailureMessages(List<String> failureMessages) {

        if(!this.failureMessages.isEmpty() && !failureMessages.isEmpty()){
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if(this.failureMessages==null){
            this.failureMessages=failureMessages;
        }
    }



    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        streetAddress = builder.streetAddress;
        price = builder.price;
        orderItems = builder.orderItems;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }


    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress streetAddress;
        private Money price;
        private List<OrderItem> orderItems;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder streetAddress(StreetAddress val) {
            streetAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder orderItems(List<OrderItem> val) {
            orderItems = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
