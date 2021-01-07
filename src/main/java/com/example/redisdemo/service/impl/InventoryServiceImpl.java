package com.example.redisdemo.service.impl;

import com.example.redisdemo.manager.RedisManager;
import com.example.redisdemo.model.request.InventoryDecreaseRequest;
import com.example.redisdemo.service.InventoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Implement reduce inventory.
 *
 * @author wuudongdong
 * @date 2021/01/06
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    @Value("${inventory.lock.key}")
    String lockKey;
    @Value("${inventory.item.prefix}")
    String prefix;
    @Resource
    RedisManager redisManager;

    @Override
    public Boolean reduceInventory(InventoryDecreaseRequest inventoryDecreaseRequest) {
        boolean isLocked = false;
        boolean isReduceSuccess = false;
        String timestamp = String.valueOf(System.nanoTime());
        try {
            isLocked = redisManager.lock(lockKey, timestamp);
            if (isLocked) {
                isReduceSuccess = redisManager.decrement(prefix + inventoryDecreaseRequest.getInventoryId(),
                        inventoryDecreaseRequest.getDecrease());
            }
            return isReduceSuccess;
        } finally {
            if (isLocked) {
                redisManager.unLock(lockKey, timestamp);
            }
        }
    }
}
