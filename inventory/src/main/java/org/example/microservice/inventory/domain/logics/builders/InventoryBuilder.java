package org.example.microservice.inventory.domain.logics.builders;

import org.example.microservice.inventory.domain.models.entities.Inventory;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryBodyRequest;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseItem;

import java.time.OffsetDateTime;

public final class InventoryBuilder {

  public Inventory buildInventory(InventoryBodyRequest body) {
    return new Inventory(body.poc(), body.dc(), body.sku(), body.quantity(), OffsetDateTime.now(), OffsetDateTime.now());
  }

  public InventoryResponseItem buildResponse(Inventory inventory) {
    return new InventoryResponseItem(inventory.pointOfContactId(), inventory.serialNumber(),
            inventory.quantity(), inventory.creationDate().toLocalDate());
  }

  public InventoryResponseItem buildEmptyResponse() {
    return new InventoryResponseItem(null, null, null, null);
  }
}
