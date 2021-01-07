package com.example.redisdemo.service;

import com.example.redisdemo.model.request.InventoryDecreaseRequest;

public interface InventoryService {
    Boolean reduceInventory(InventoryDecreaseRequest inventoryDecreaseRequest);
}
