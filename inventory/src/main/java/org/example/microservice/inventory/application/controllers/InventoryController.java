package org.example.microservice.inventory.application.controllers;
import org.example.microservice.inventory.domain.logics.converters.ResponseConverter;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryParams;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponse;
import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseItem;
import org.example.microservice.inventory.domain.models.vos.requests.InventoryBodyRequest;
import org.example.microservice.inventory.application.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  private final ResponseConverter responseConverter;

  @Autowired
  public InventoryController(InventoryService inventoryService, ResponseConverter responseConverter) {
    this.inventoryService = inventoryService;
    this.responseConverter = responseConverter;
  }

  @PostMapping
  public ResponseEntity<InventoryResponseItem> postInventories(
          @RequestBody final InventoryBodyRequest body){

    return Stream.of(body)
            .map(inventoryService::createInventory)
            .map((response -> new ResponseEntity<>(response, HttpStatus.OK)))
            .findFirst().orElse(new ResponseEntity<>(InventoryResponseItem.empty(), HttpStatus.NOT_FOUND));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<InventoryResponse> getInventories(
          @RequestParam final String sn,
          @RequestParam(required = false) final String poc,
          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          final LocalDate from)
  {

    var response = Stream.of(InventoryParams.of(sn, poc, from))
            .flatMap(inventoryService::getInventory)
            .map(responseConverter::fromInventoryToResponse)
            .toList();

    return Optional.of(new ResponseEntity<>(new InventoryResponse(response), HttpStatus.OK))
            .orElse(new ResponseEntity<>(new InventoryResponse(null), HttpStatus.NOT_FOUND));
  }

}
