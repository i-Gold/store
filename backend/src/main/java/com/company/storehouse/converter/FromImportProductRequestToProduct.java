package com.company.storehouse.converter;

import com.company.storehouse.controller.request.ImportProductRequest;
import com.company.storehouse.model.Product;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class FromImportProductRequestToProduct extends AbstractConverter<ImportProductRequest, Product> {

    @Override
    protected Product convert(ImportProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .manufacturer(request.getManufacturer())
                .weight(request.getWeight())
                .producedAt(request.getProducedAt())
                .expiredAt(request.getExpiredAt())
                .build();
    }

}
