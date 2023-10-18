package org.example.microservice.inventory.domain.logics.builders;

import org.example.microservice.inventory.domain.models.entities.Inventory;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryIngestionBodyRequest;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryModificationBodyRequest;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseItem;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public final class InventoryBuilder {

  public Inventory buildInventory(InventoryIngestionBodyRequest body) {
    return new Inventory(null, body.pointOfContact(), body.distributionCenter(), body.serialNumber(), body.quantity(),
            OffsetDateTime.now(), OffsetDateTime.now());
  }

  public InventoryResponseItem buildResponse(Inventory inventory) {
    return new InventoryResponseItem(inventory.pointOfContactId(), inventory.serialNumber(),
            inventory.quantity(), inventory.creationDate().toLocalDate());
  }

  public Inventory updateInventory(Inventory old, InventoryModificationBodyRequest body) {
    return new Inventory(
            old.id(),
            old.pointOfContactId(),
            old.distributionCenterId(),
            old.serialNumber(),
            body.quantity(),
            old.creationDate(),
            OffsetDateTime.now());
  }
}
