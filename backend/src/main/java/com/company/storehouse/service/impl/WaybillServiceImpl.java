package com.company.storehouse.service.impl;

import com.company.storehouse.controller.exception.ApproveWaybillException;
import com.company.storehouse.controller.exception.ProductsNotFoundException;
import com.company.storehouse.model.Product;
import com.company.storehouse.model.User;
import com.company.storehouse.model.Waybill;
import com.company.storehouse.model.enumeration.ProcessingStatus;
import com.company.storehouse.model.enumeration.RoleName;
import com.company.storehouse.model.enumeration.WaybillType;
import com.company.storehouse.repository.ProductRepository;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.repository.WaybillRepository;
import com.company.storehouse.service.WaybillService;
import com.company.storehouse.util.CSVUtil;
import com.company.storehouse.util.FreeSlotsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class WaybillServiceImpl implements WaybillService {

    private WaybillRepository waybillRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    @Value("${storage.slots.max}")
    private String maxSlotsInStorage;

    @Override
    public Waybill createWaybillForImport() {
        User currentUser = userRepository.getCurrentUser();
        List<Product> productsForImport = productRepository.getProductsForImport(currentUser.getId());
        checkEmptyProducts(productsForImport);
        FreeSlotsUtil.check(productsForImport.size(), productRepository, maxSlotsInStorage);
        return saveWaybillForImport(currentUser, productsForImport);
    }

    private void checkEmptyProducts(List<Product> products) {
        if (products.isEmpty()) {
            log.warn("You haven't any product for waybill!");
            throw new ProductsNotFoundException();
        }
    }

    private Waybill saveWaybillForImport(User currentUser, List<Product> productsForImport) {
        Waybill waybill = new Waybill();
        waybill.setOwnerId(currentUser.getId());
        waybill.setType(WaybillType.INPUT);
        if (currentUser.getRole().getName().equals(RoleName.MANAGER)) {
            waybill.setStatus(ProcessingStatus.PENDING);
            productsForImport.forEach(product -> product.setIsPendingImport(Boolean.TRUE));
        } else {
            waybill.setStatus(ProcessingStatus.COMPLETED);
            waybill.setIsApproved(Boolean.TRUE);
            waybill.setApproverId(currentUser.getId());
            productsForImport.forEach(product -> {
                product.setIsAvailable(Boolean.TRUE);
                product.setImportedAt(LocalDateTime.now());
            });
            waybill.setCreatedAt(LocalDateTime.now());
        }
        waybill.setProducts(productsForImport);
        return waybillRepository.save(waybill);
    }

    @Override
    public Waybill createWaybillForExport() {
        User currentUser = userRepository.getCurrentUser();
        List<Product> productsForExport = productRepository.getProductsForExport(currentUser.getId());
        checkEmptyProducts(productsForExport);
        return saveWaybillForExport(currentUser, productsForExport);
    }

    private Waybill saveWaybillForExport(User currentUser, List<Product> productsForExport) {
        Waybill waybill = new Waybill();
        waybill.setOwnerId(currentUser.getId());
        waybill.setType(WaybillType.OUTPUT);
        if (currentUser.getRole().getName().equals(RoleName.MANAGER)) {
            waybill.setStatus(ProcessingStatus.PENDING);
            productsForExport.forEach(product -> {
                product.setIsPendingExport(Boolean.TRUE);
            });
        } else {
            waybill.setStatus(ProcessingStatus.COMPLETED);
            waybill.setIsApproved(Boolean.TRUE);
            waybill.setApproverId(currentUser.getId());
            productsForExport.forEach(product -> {
                product.setIsAvailable(Boolean.FALSE);
                product.setIsExported(Boolean.TRUE);
            });
            waybill.setCreatedAt(LocalDateTime.now());
        }
        waybill.setProducts(productsForExport);
        return waybillRepository.save(waybill);
    }

    @Override
    public Waybill approveOrRejectWaybill(Long id, ProcessingStatus status) {
        Waybill waybill = waybillRepository.findById(id).orElse(null);
        boolean isValid = validateRequest(waybill, status);
        if (!isValid) {
            throw new ApproveWaybillException();
        }
        FreeSlotsUtil.check(waybill.getProducts().size(), productRepository, maxSlotsInStorage);
        User user = userRepository.getCurrentUser();
        waybill.setApproverId(user.getId());
        waybill.setStatus(status);
        waybill.setIsApproved(Boolean.TRUE);
        return createAvailableWaybill(waybill);
    }

    private boolean validateRequest(Waybill waybill, ProcessingStatus status) {
        if (waybill == null) {
            return false;
        }
        if (!waybill.getStatus().equals(ProcessingStatus.PENDING)) {
            return false;
        }
        if (waybill.getIsApproved()) {
            return false;
        }
        return !status.equals(ProcessingStatus.PENDING);
    }

    private Waybill createAvailableWaybill(Waybill waybill) {
        if (waybill.getStatus().equals(ProcessingStatus.COMPLETED)) {
            if (waybill.getType().equals(WaybillType.INPUT)) {
                waybill.getProducts().forEach(product -> {
                    product.setIsAvailable(Boolean.TRUE);
                    product.setIsPendingImport(Boolean.FALSE);
                    product.setImportedAt(LocalDateTime.now());
                });
            } else {
                waybill.getProducts().forEach(product -> {
                    product.setIsAvailable(Boolean.FALSE);
                    product.setIsPendingExport(Boolean.FALSE);
                    product.setIsExported(Boolean.TRUE);
                });
            }
        } else {
            if (waybill.getType().equals(WaybillType.INPUT)) {
                waybill.getProducts().forEach(product -> {
                    product.setIsPendingImport(Boolean.FALSE);
                    product.setIsRejectedForImport(Boolean.TRUE);
                });
            } else {
                waybill.getProducts().forEach(product -> {
                    product.setIsPendingExport(Boolean.FALSE);
                    product.setForExport(Boolean.FALSE);
                });
            }
        }
        waybill.setCreatedAt(LocalDateTime.now());
        return waybillRepository.save(waybill);
    }

    @Override
    @Transactional(readOnly = true)
    public Waybill getById(Long id) {
        if (id == null) {
            return null;
        }
        Waybill waybill = waybillRepository.findById(id).orElse(null);
        if (waybill == null) {
            log.error("IN findById — no waybill found by id: {}", id);
        }
        return waybill;
    }

    @Override
    public Waybill uploadFromCSV(MultipartFile csvFile) throws IOException {
        List<Product> productsFromCSV = CSVUtil.parseFromCSV(csvFile);
        checkEmptyProducts(productsFromCSV);
        FreeSlotsUtil.check(productsFromCSV.size(), productRepository, maxSlotsInStorage);
        User currentUser = userRepository.getCurrentUser();
        Waybill waybill = new Waybill();
        waybill.setStatus(ProcessingStatus.PENDING);
        waybill.setType(WaybillType.INPUT);
        waybill.setOwnerId(currentUser.getId());
        productsFromCSV.forEach(product -> {
            product.setImporterId(currentUser.getId());
            product.setIsPendingImport(Boolean.TRUE);
        });
        waybill.setProducts(productsFromCSV);
        return waybillRepository.save(waybill);
    }

    @Override
    @Transactional(readOnly = true)
    public void downloadInCSV(Long waybillId, HttpServletResponse response) throws IOException {
        Waybill waybill = waybillRepository.findById(waybillId).orElse(null);
        final String fileName = String.format("%s_%s_%s.csv", Objects.requireNonNull(waybill).getId(), waybill.getType(), waybill.getStatus());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        CSVUtil.write(response.getWriter(), waybill.getProducts());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Waybill> getAllWaybills(Pageable pageable) {
        return waybillRepository.findAllByOrderByIdDesc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Waybill> getWaybillsForConfirmationImport(Pageable pageable) {
        return waybillRepository.findAllByStatusAndTypeAndIsApprovedFalseAndCreatedAtNull(ProcessingStatus.PENDING, WaybillType.INPUT, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Waybill> getWaybillsForConfirmationExport(Pageable pageable) {
        return waybillRepository.findAllByStatusAndTypeAndIsApprovedFalseAndCreatedAtNull(ProcessingStatus.PENDING, WaybillType.OUTPUT, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Waybill> getWaybillsForConfirmationAutoExport(Pageable pageable) {
        return waybillRepository.findAllByStatusAndTypeAndIsApprovedFalseAndCreatedAtNull(ProcessingStatus.PENDING, WaybillType.AUTO_OUTPUT, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Waybill> getRejectedWaybills(Pageable pageable) {
        return waybillRepository.getRejectedWaybills(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Waybill> getPendingWaybillsOfCurrentUser(Pageable pageable) {
        User user = userRepository.getCurrentUser();
        return waybillRepository.findAllByOwnerIdAndStatusOrderByIdDesc(user.getId(), ProcessingStatus.PENDING, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Waybill> getCompletedWaybillsOfCurrentUser(Pageable pageable) {
        User user = userRepository.getCurrentUser();
        return waybillRepository.findAllByOwnerIdAndStatusOrderByIdDesc(user.getId(), ProcessingStatus.COMPLETED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Waybill> getRejectedWaybillsOfCurrentUser(Pageable pageable) {
        User user = userRepository.getCurrentUser();
        return waybillRepository.findAllByOwnerIdAndStatusOrderByIdDesc(user.getId(), ProcessingStatus.REJECTED, pageable);
    }

    @Autowired
    public void setWaybillRepository(WaybillRepository waybillRepository) {
        this.waybillRepository = waybillRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

}
