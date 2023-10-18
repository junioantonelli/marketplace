package org.example.microservice.inventory.application.services;

import org.example.microservice.inventory.domain.logics.builders.InventoryBuilder;
import org.example.microservice.inventory.domain.logics.converters.ResponseConverter;
import org.example.microservice.inventory.domain.models.entities.Inventory;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryModificationBodyRequest;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryParams;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryIngestionBodyRequest;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseItem;
import org.example.microservice.inventory.infrastructure.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Stream;

@Service
public class InventoryService {

  private final InventoryRepository inventoryRepository;
  private final InventoryBuilder inventoryBuilder;
  private final ResponseConverter responseConverter;
  @Autowired
  public InventoryService(InventoryRepository inventoryRepository,
                          InventoryBuilder inventoryBuilder,
                          ResponseConverter responseConverter) {
    this.inventoryRepository = inventoryRepository;
    this.inventoryBuilder = inventoryBuilder;
    this.responseConverter = responseConverter;
  }

  public Collection<InventoryResponseItem> retrieveInventory(InventoryParams params) {
    return Stream.of(params)
            .flatMap(parameters -> inventoryRepository.findBySerialNumber(parameters.sn()).stream())
            .map(responseConverter::fromInventoryToResponse)
            .toList();
  }

  public InventoryResponseItem ingestInventory(InventoryIngestionBodyRequest inventoryIngestionBodyRequest) {
    return Stream.of(inventoryIngestionBodyRequest)
            .map(inventoryBuilder::buildInventory)
            .map(inventoryRepository::insert)
            .map(inventoryBuilder::buildResponse)
            .findFirst().orElse(null);
  }

  public InventoryResponseItem modifyInventory(InventoryModificationBodyRequest body) {
    return Stream.of(body)
            .map(InventoryModificationBodyRequest::serialNumber)
            .flatMap(sn -> inventoryRepository.findBySerialNumber(sn).stream())
            .map(old -> inventoryBuilder.updateInventory(old, body))
            .map(inventoryRepository::save)
            .map(inventoryBuilder::buildResponse)
            .findFirst().orElse(null);
  }
  public InventoryResponseItem removeInventory(InventoryParams params) {
    return Stream.of(params)
            .flatMap(parameters -> inventoryRepository.findBySerialNumber(parameters.sn()).stream())
            .map(inventory -> {inventoryRepository.deleteById(inventory.id().toString());return inventory;})
            .map(inventoryBuilder::buildResponse)
            .findFirst().orElse(null);
  }
}
