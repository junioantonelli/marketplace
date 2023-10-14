package org.example.microservice.inventory.domain.models.vos.requests;

import java.time.LocalDate;

public record InventoryParams(String sn, String poc, LocalDate from) {

  public static InventoryParams of(String sn, String poc, LocalDate from){
    return new InventoryParams(sn, poc, from);
  }
}
