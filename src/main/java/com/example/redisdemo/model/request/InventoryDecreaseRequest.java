package com.example.redisdemo.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Reduce Inventory Request.
 *
 * @author wuudongdong
 * @date 2021/01/06
 */
@Data
public class InventoryDecreaseRequest {
    @NotNull
    private Long inventoryId;
    @NotNull
    private Long decrease;
}
