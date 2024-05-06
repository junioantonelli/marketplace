package org.example.microservice.inventory.domain.models.vos.responses;

import java.util.Collection;
import java.util.List;

public record InventoryResponse(Collection<InventoryResponseItem> inventoryIncredibleList) {
    public static InventoryResponse empty() {
        return new InventoryResponse(List.of());
    }
}
