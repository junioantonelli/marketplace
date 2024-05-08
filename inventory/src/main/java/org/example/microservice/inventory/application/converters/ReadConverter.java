package org.example.microservice.inventory.application.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

//@ReadingConverter
public final class ReadConverter
        implements Converter<Date, OffsetDateTime> {

  @Override
  public OffsetDateTime convert(final Date source) {
    return OffsetDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
  }
}
