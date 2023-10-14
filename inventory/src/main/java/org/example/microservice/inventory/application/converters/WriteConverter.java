package org.example.microservice.inventory.application.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.OffsetDateTime;
import java.util.Date;

@WritingConverter
public final class WriteConverter
        implements Converter<OffsetDateTime, Date> {

  @Override
  public Date convert(final OffsetDateTime source) {
    return Date.from(source.toInstant());
  }
}