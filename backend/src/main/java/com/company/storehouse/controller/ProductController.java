package com.company.storehouse.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.company.storehouse.controller.request.ExportProductRequest;
import com.company.storehouse.controller.request.ImportProductRequest;
import com.company.storehouse.controller.response.View;
import com.company.storehouse.controller.response.pagination.ApiPageable;
import com.company.storehouse.controller.response.pagination.PageableResponse;
import com.company.storehouse.model.Product;
import com.company.storehouse.service.ProductService;
import com.company.storehouse.validation.MaxSizeConstraint;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Prepare products for import", notes = "Please, use token.")
    @PostMapping("/products")
    @JsonView(View.Product.class)
    public ResponseEntity<List<Product>> addProductsForImport(@RequestBody
                                                              @NotEmpty(message = "Input list cannot be empty.")
                                                              @MaxSizeConstraint List<@Valid ImportProductRequest> requestList) {

        return ResponseEntity.ok(productService.addProductsForImport(requestList));
    }

    @ApiOperation(value = "Prepare products for export", notes = "Please, use token.")
    @PutMapping("/products/prepare-export")
    @JsonView(View.Product.class)
    public ResponseEntity<List<Product>> addProductsForExport(@RequestBody ExportProductRequest request) {
        return ResponseEntity.ok(productService.addProductsForExport(request));
    }

    @ApiOperation(value = "Get product by ID", notes = "Please, use token.")
    @GetMapping("/products/{id}")
    @JsonView(View.Product.class)
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @ApiOperation(value = "Get all available products in stock", notes = "Please, use token.")
    @ApiPageable
    @GetMapping("/products")
    @JsonView(View.Product.class)
    public ResponseEntity<PageableResponse<Product>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(productService.getAllProducts(pageable)));
    }

    @ApiOperation(value = "Get pending export products of current user", notes = "Please, use token.")
    @ApiPageable
    @GetMapping("/products/pending-export")
    @JsonView(View.Product.class)
    public ResponseEntity<PageableResponse<Product>> getPendingExportProductsOfCurrentUser(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(productService.getPendingExportProductsOfCurrentUser(pageable)));
    }

    @ApiOperation(value = "Get products for confirming import ***", notes = "Please, use token.")
    @ApiPageable
    @PreAuthorize("hasAuthority('IMPORT_EXPORT')")
    @GetMapping("/products/for-import")
    @JsonView(View.Product.class)
    public ResponseEntity<PageableResponse<Product>> getNewProductsForImport(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(productService.getNewProductsForImport(pageable)));
    }

    @ApiOperation(value = "Get products for confirming export ***", notes = "Please, use token.")
    @ApiPageable
    @PreAuthorize("hasAuthority('IMPORT_EXPORT')")
    @GetMapping("/products/for-export")
    @JsonView(View.Product.class)
    public ResponseEntity<PageableResponse<Product>> getProductsForExport(Pageable pageable) {
        return ResponseEntity.ok(new PageableResponse<>(productService.getProductsForExport(pageable)));
    }

    @ApiOperation(value = "Get products in stock with filtered parameters", notes = "Please, use token.")
    @ApiPageable
    @GetMapping("/products/filtered")
    @JsonView(View.Product.class)
    public ResponseEntity<PageableResponse<Product>> getFilteredProducts(@RequestParam(required = false) String name,
                                                                         @RequestParam(required = false) String manufacturer,
                                                                         @DateTimeFormat(pattern = "dd-MM-yyyy") @RequestParam(required = false) LocalDate expiredAt,
                                                                         Integer page,
                                                                         Integer size,
                                                                         Pageable pageable) {

        return ResponseEntity.ok(new PageableResponse<>(productService.getProductsWithFilter(name, manufacturer, expiredAt, page, size, pageable)));
    }

}
