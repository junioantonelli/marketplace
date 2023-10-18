package org.example.microservice.inventory.domain.models.vos.requests;

public record InventoryModificationBodyRequest(String serialNumber, Integer quantity) {
}
