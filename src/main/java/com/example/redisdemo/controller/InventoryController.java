package com.example.redisdemo.controller;

import com.example.redisdemo.model.request.InventoryDecreaseRequest;
import com.example.redisdemo.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Inventory Controller.
 *
 * @author wuudongdong
 * @date 2021/01/06
 */
@RestController
public class InventoryController {

    @Resource
    InventoryService inventoryService;

    @PostMapping("inventory")
    public String reduceInventory(@RequestBody @Valid InventoryDecreaseRequest inventoryDecreaseRequest) {
        return inventoryService.reduceInventory(inventoryDecreaseRequest) ? "success" : "failed";
    }
}
