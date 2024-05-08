package org.example.microservice.inventory.application.converters;

import org.bson.Document;
import org.example.microservice.inventory.domain.models.entities.Inventory;
import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class InventoryReadingConverter implements Converter<Document, Inventory> {

    @Override
    public Inventory convert(Document doc) {
        Inventory inventory = new Inventory(
                doc.getObjectId("_id"),
                (String)doc.get("pointOfContactId"),
                (String)doc.get("distributionCenterId"),
                (String)doc.get("serialNumber"),
                (Integer)doc.get("quantity"),
                OffsetDateTime.ofInstant(doc.getDate("creationDate").toInstant(), ZoneOffset.UTC),
                OffsetDateTime.ofInstant(doc.getDate("lastUpdate").toInstant(), ZoneOffset.UTC)
        );

        return inventory;
    }
}
