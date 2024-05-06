package org.example.microservice.inventory.application.controllers;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryModificationBodyRequest;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryParams;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponse;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseItem;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryIngestionBodyRequest;
import org.example.microservice.inventory.application.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
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
  public ResponseEntity getInventories(
          @RequestParam final String sn,
          @RequestParam(required = false) final String poc,
          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          final LocalDate from)
  {

    var response = Stream.of(InventoryParams.of(sn, poc, from))
            .flatMap(param -> inventoryService.retrieveInventory(param).stream())
            .toList();

    return Optional.of(response)
            .filter(Predicate.not(CollectionUtils::isEmpty))
            .map(ResponseEntity::ok)
            .orElse(new ResponseEntity<>(List.of(InventoryResponseItem.empty()), HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Collection<InventoryResponseItem>> postInventories(
          @RequestBody final InventoryIngestionBodyRequest body){

    var a = Stream.of(body)
            .map(inventoryService::ingestInventory)
            .filter(Objects::nonNull)
            .map((response -> new ResponseEntity<>(response, HttpStatus.OK)))
            .findFirst()
            .orElse(new ResponseEntity<>(List.of(InventoryResponseItem.empty()), HttpStatus.INTERNAL_SERVER_ERROR));
    return a;
  }

  @PutMapping
  public ResponseEntity<Collection<InventoryResponseItem>> putInventories(
          @RequestBody final InventoryModificationBodyRequest body){

    return Stream.of(body)
            .map(inventoryService::modifyInventory)
            .filter(Objects::nonNull)
            .map((response -> new ResponseEntity<>(response, HttpStatus.OK)))
            .findFirst()
            .orElse(new ResponseEntity<>(List.of(InventoryResponseItem.empty()), HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<InventoryResponseItem>> deleteInventories(
          @RequestParam final String sn){
    var response = Stream.of(InventoryParams.of(sn, null, null))
            .map(inventoryService::removeInventory).findFirst();

    return response
            .filter(Predicate.not(CollectionUtils::isEmpty))
            .map(res -> new ResponseEntity<>(res, HttpStatus.OK))
            .orElse(new ResponseEntity<>(List.of(new InventoryResponseItem(null,null,null,null)), HttpStatus.NOT_FOUND));
  }

}
