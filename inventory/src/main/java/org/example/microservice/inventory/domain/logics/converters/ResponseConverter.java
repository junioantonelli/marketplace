package org.example.microservice.inventory.domain.logics.converters;

import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseItem;
import org.example.microservice.inventory.domain.models.entities.Inventory;
import org.springframework.stereotype.Component;

@Component
public final class ResponseConverter {
  public InventoryResponseItem fromInventoryToResponse(Inventory inv) {
    return new InventoryResponseItem(inv.pointOfContactId(), inv.serialNumber(), inv.quantity(), inv.lastUpdate().toLocalDate());
  }
}
