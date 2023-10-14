package org.example.microservice.inventory.domain.models.vos.responses;

import java.time.LocalDate;

public record InventoryResponseItem(
        String poc,
        String sn,
        Integer qtt,
        LocalDate lu
) {
  public static InventoryResponseItem empty() {
    return new InventoryResponseItem(null, null, null, null);
  }
}
