package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTesting {
    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    String USERNAME = null;
    String PASSWORD = null;

    @Before
    public void setUp() {
        cartController = new CartController();
        USERNAME = "ducnv";
        PASSWORD = "12345678";
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

        User user = new User(USERNAME,PASSWORD);
        Cart cart = new Cart();
        user.setId(0);
        user.setCart(cart);
        when(userRepo.findByUsername("ducnv")).thenReturn(user);

        Item item = new Item("phone", BigDecimal.valueOf(2.2),"good");
        item.setId(1L);
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void addToCartSuccess() {
        ModifyCartRequest r = new ModifyCartRequest("ducnv",1L,1);

        ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertNotNull(c);

    }



    @Test
    public void addToCartFailUsername() {
        ModifyCartRequest r = new ModifyCartRequest("ducnv123",1L,1);
        ResponseEntity<Cart> response = cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeCartSuccess() {
        ModifyCartRequest r = new ModifyCartRequest("ducnv",1L,1);
        ResponseEntity<Cart> response = cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        r = new ModifyCartRequest("ducnv",1L,1);
        response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertNotNull(c);

    }

    @Test
    public void removeCartFailByItem() {
        ModifyCartRequest r = new ModifyCartRequest("ducnv",100L,1);
        ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
