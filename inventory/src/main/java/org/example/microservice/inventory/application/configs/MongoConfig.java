package org.example.microservice.inventory.application.configs;

import org.example.microservice.inventory.application.converters.ReadConverter;
import org.example.microservice.inventory.application.converters.WriteConverter;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
@Import(value = MongoAutoConfiguration.class)
public class MongoConfig {
  @Bean
  public MongoCustomConversions customConversions() {
    return new MongoCustomConversions(
            List.of(
                    new ReadConverter(), new WriteConverter()));
  }
}
