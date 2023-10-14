package org.example.microservice.inventory.domain.models.vos.responses;

import java.util.Collection;

public record InventoryResponse(Collection<InventoryResponseItem> inventoryList) {
}
