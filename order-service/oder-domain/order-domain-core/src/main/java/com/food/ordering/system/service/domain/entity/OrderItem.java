package com.food.ordering.system.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobjects.Money;
import com.food.ordering.system.domain.valueobjects.OrderId;
import com.food.ordering.system.service.domain.objectvalue.OrderItemId;


public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId;
    private final Product product;
    private int quantity;
    private final Money price;
    private final Money subTotals;

    boolean isPriceValidate(){
               return price.isGreaterThanZero() &&
                       price.equals(product.getMoney() ) &&
                        price.multiply(quantity).equals(subTotals);

    }


    void initializeOrderItem(OrderId orderId,OrderItemId orderItemId) {
        this.orderId=orderId;
        super.setId(orderItemId);
    }

    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subTotals = builder.subTotals;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotals() {
        return subTotals;
    }




    public static final class Builder {
        private OrderItemId orderItemId;
        private Product product;
        private int quantity;
        private Money price;
        private Money subTotals;

        private Builder() {
        }

        public Builder orderItemId(OrderItemId orderItemId) {
            this.orderItemId = orderItemId;
            return this;
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subTotals(Money val) {
            subTotals = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
