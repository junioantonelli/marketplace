package org.example.microservice.inventory.domain.models.vos.requests;

public record InventoryBodyRequest(String sku, String poc, String dc, Integer quantity){}