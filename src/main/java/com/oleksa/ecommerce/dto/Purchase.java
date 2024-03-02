package com.oleksa.ecommerce.dto;

import com.oleksa.ecommerce.entity.Address;
import com.oleksa.ecommerce.entity.Users;
import com.oleksa.ecommerce.entity.Order;
import com.oleksa.ecommerce.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Users users;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

}
