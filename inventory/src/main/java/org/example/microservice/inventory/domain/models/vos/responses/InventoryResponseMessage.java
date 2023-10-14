package org.example.microservice.inventory.domain.models.vos.responses;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record InventoryResponseMessage(String message) {
}
