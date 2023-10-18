package org.example.microservice.inventory.domain.models.vos.requests;

public record InventoryIngestionBodyRequest(String serialNumber, String pointOfContact, String distributionCenter, Integer quantity){}