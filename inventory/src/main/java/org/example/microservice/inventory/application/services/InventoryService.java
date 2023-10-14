package org.example.microservice.inventory.application.services;

import org.example.microservice.inventory.domain.logics.builders.InventoryBuilder;
import org.example.microservice.inventory.domain.models.entities.Inventory;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryParams;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryBodyRequest;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseItem;
import org.example.microservice.inventory.infrastructure.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class InventoryService {

  private InventoryRepository inventoryRepository;
  private InventoryBuilder inventoryBuilder;

  @Autowired
  public InventoryService(InventoryRepository inventoryRepository,
                          InventoryBuilder inventoryBuilder) {
    this.inventoryRepository = inventoryRepository;
    this.inventoryBuilder = inventoryBuilder;
  }

//  public Collection<Inventory> retrieveInventory(GetV1Params params) {
//
//    return Stream.of(params)
//            .map(parameters -> inventoryCustomRepository.findInventoryByPocIdAndUpdatedAfter(
//                    OffsetDateTime.of(parameters.validFrom(),
//                    LocalTime.MIDNIGHT, ZoneOffset.UTC), parameters.poc()))
//            .flatMap(Collection::stream)
//            .toList();
//
//  }
  public Stream<Inventory> getInventory(InventoryParams params) {
    return Stream.of(params)
            .flatMap(parameters -> inventoryRepository.findBySerialNumber(parameters.sn()).stream());
  }

  public InventoryResponseItem createInventory(InventoryBodyRequest inventoryBodyRequest) {
    return Stream.of(inventoryBodyRequest)
            .map(inventoryBuilder::buildInventory)
            .map(inventoryRepository::insert)
            .map(inventoryBuilder::buildResponse)
            .findFirst().orElse(inventoryBuilder.buildEmptyResponse());
  }
}
