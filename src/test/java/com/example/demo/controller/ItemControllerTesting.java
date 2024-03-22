package com.example.demo.controller;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

public class ItemControllerTesting {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void testGetItems() {
        List<Item> items = new ArrayList<>(2);
        items.add(new Item("phone", BigDecimal.valueOf(2.2),"good"));
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(1, retrievedItems.size());
    }

    @Test
    public void testGetItemsByName() {
        List<Item> items = new ArrayList<>(2);
        items.add(new Item("phone", BigDecimal.valueOf(2.2),"good"));
        when(itemRepository.findByName("phone")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("phone");

        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(1, retrievedItems.size());
    }

}
