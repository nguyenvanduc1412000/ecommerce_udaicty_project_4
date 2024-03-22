package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTesting {

    private OrderController orderController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);
    String USERNAME = "ducnv";
    String PASSWORD = "12345678";

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        User user = new User(USERNAME,PASSWORD);
        user.setId(0L);
        List<Item> items = new ArrayList<>(1);
        items.add(new Item("phone", BigDecimal.valueOf(2.2),"good"));

        Cart cart = new Cart(items,user,BigDecimal.valueOf(2.2));
        user.setId(0);
        user = new User(USERNAME,PASSWORD);
        cart.setId(0L);
        user.setCart(cart);
        when(userRepo.findByUsername("ducnv")).thenReturn(user);
    }

    @Test
    public void testSubmitFailUsername() {

        ResponseEntity<UserOrder> response = orderController.submit("ducnv2233");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void testSubmitCheckSizeItem() {

        ResponseEntity<UserOrder> response = orderController.submit("ducnv");
        UserOrder order = response.getBody();
        assertEquals(1, order.getItems().size());

    }

    @Test
    public void testGetOrders() {

        orderController.submit(USERNAME);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(USERNAME);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }




}
