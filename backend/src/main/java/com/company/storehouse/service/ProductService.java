package com.company.storehouse.service;

import com.company.storehouse.controller.request.ExportProductRequest;
import com.company.storehouse.controller.request.ImportProductRequest;
import com.company.storehouse.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {

    List<Product> addProductsForImport(List<ImportProductRequest> request);

    List<Product> addProductsForExport(ExportProductRequest request);

    Product getById(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    Page<Product> getPendingExportProductsOfCurrentUser(Pageable pageable);

    Page<Product> getProductsWithFilter(String name, String manufacturer, LocalDate expiredAt, Integer page, Integer size, Pageable pageable);

    Page<Product> getNewProductsForImport(Pageable pageable);

    Page<Product> getProductsForExport(Pageable pageable);

}
