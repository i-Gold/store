package com.company.storehouse.repository;

import com.company.storehouse.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT DISTINCT p.* FROM products p WHERE p.is_available IS TRUE AND p.imported_at IS NOT NULL " +
            "ORDER BY p.imported_at DESC " +
            "LIMIT ?#{#pageable.offset}, ?#{#pageable.offset + #pageable.pageSize}",
            countQuery = "SELECT COUNT(DISTINCT p.id) FROM products p WHERE p.is_available IS TRUE AND p.imported_at IS NOT NULL",
            nativeQuery = true)
    Page<Product> getAllUniqueProducts(Pageable pageable);

    Page<Product> findByIsAvailableFalseAndForExportFalseAndIsExportedFalse(Pageable pageable);

    Page<Product> findByIsAvailableTrueAndForExportTrueAndIsExportedFalse(Pageable pageable);

    @Query("FROM Product p WHERE p.importerId = :importerId AND p.exporterId IS NULL AND p.isAvailable IS FALSE " +
            "AND p.isPendingImport IS FALSE AND p.isPendingExport IS FALSE " +
            "AND p.importedAt IS NULL AND p.forExport IS FALSE AND p.isExported IS FALSE AND isRejectedForImport IS FALSE")
    List<Product> getProductsForImport(@Param("importerId") Long id);

    @Query("FROM Product p WHERE p.exporterId = :exporterId AND p.importerId IS NOT NULL AND p.isAvailable IS TRUE " +
            "AND p.isPendingImport IS FALSE AND p.isPendingExport IS FALSE " +
            "AND p.importedAt IS NOT NULL AND p.forExport IS TRUE AND p.isExported IS FALSE AND isRejectedForImport IS FALSE")
    List<Product> getProductsForExport(@Param("exporterId") Long id);

    @Query(value = "SELECT count(DISTINCT p.id) FROM products p", nativeQuery = true)
    BigInteger countAllProductsInStock();

    @Query(value = "SELECT p.* FROM products p WHERE p.is_available IS TRUE AND CURDATE() >= p.expired_at AND p.for_export IS FALSE", nativeQuery = true)
    List<Product> getExpiredProducts();

}
