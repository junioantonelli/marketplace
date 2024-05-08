package org.example.microservice.inventory.application.controllers;

import org.example.microservice.inventory.application.services.InventoryService;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryIngestionBodyRequest;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryModificationBodyRequest;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryParams;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  @Autowired
  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<InventoryResponseItem>> getInventories(
          @RequestParam final String sn,
          @RequestParam(required = false) final String poc,
          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          final LocalDate from)
  {

    return Stream.of(InventoryParams.of(sn, poc, from))
            .map(inventoryService::retrieveInventory)
            .filter(Predicate.not(CollectionUtils::isEmpty))
            .map(ResponseEntity::ok)
            .findFirst()
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of()));
  }

  @PostMapping
  public ResponseEntity<Collection<InventoryResponseItem>> postInventories(
          @RequestBody final InventoryIngestionBodyRequest body){

      return Stream.of(body)
            .map(inventoryService::ingestInventory)
            .filter(Predicate.not(CollectionUtils::isEmpty))
            .map(ResponseEntity::ok)
            .findFirst()
            .orElse(ResponseEntity.internalServerError().body(List.of()));
  }

  @PutMapping
  public ResponseEntity<Collection<InventoryResponseItem>> putInventories(
          @RequestBody final InventoryModificationBodyRequest body){

    return Stream.of(body)
            .map(inventoryService::modifyInventory)
            .filter(Predicate.not(CollectionUtils::isEmpty))
            .map((response -> new ResponseEntity<>(response, HttpStatus.OK)))
            .findFirst()
            .orElse(new ResponseEntity<>(List.of(InventoryResponseItem.empty()), HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<InventoryResponseItem>> deleteInventories(
          @RequestParam final String sn){

    return Stream.of(InventoryParams.of(sn, null, null))
            .map(inventoryService::removeInventory)
            .filter(Predicate.not(CollectionUtils::isEmpty))
            .map(ResponseEntity::ok)
            .findFirst()
            .orElse(new ResponseEntity<>(List.of(new InventoryResponseItem(null,null,null,null)), HttpStatus.NOT_FOUND));
  }

}
