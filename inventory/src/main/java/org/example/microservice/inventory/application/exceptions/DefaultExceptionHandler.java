package org.example.microservice.inventory.application.exceptions;

import org.example.microservice.inventory.domain.models.vos.responses.InventoryResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionHandler {

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  InventoryResponseMessage notFoundMessage(final MissingServletRequestParameterException e){
    return new InventoryResponseMessage(String.format("Missing Parameter: %s is not present on request", e.getParameterName()));
  }
}
