package org.example.microservice.inventory.application.converters;

import org.bson.Document;
import org.example.microservice.inventory.domain.models.entities.Inventory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;
import java.util.Objects;

public class InventoryWritingConverter implements Converter<Inventory, Document> {

    @Override
    public Document convert(Inventory inventory) {
        Document doc = new Document();

        if (Objects.nonNull(inventory.id()))
            doc.append("_id", inventory.id());
        doc.append("pointOfContactId", inventory.pointOfContactId());
        doc.append("distributionCenterId", inventory.distributionCenterId());
        doc.append("serialNumber", inventory.serialNumber());
        doc.append("quantity", inventory.quantity());
        doc.append("creationDate", Date.from(inventory.creationDate().toInstant()));
        doc.append("lastUpdate", new Date());

        return doc;
    }
}
