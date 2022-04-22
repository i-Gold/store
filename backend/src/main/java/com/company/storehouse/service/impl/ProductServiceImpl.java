package com.company.storehouse.service.impl;

import com.company.storehouse.controller.request.ExportProductRequest;
import com.company.storehouse.controller.request.ImportProductRequest;
import com.company.storehouse.model.Product;
import com.company.storehouse.model.User;
import com.company.storehouse.repository.ProductRepository;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.service.ProductService;
import com.company.storehouse.util.FreeSlotsUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ModelMapper modelMapper;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Value("${storage.slots.max}")
    private String maxSlotsInStorage;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> addProductsForImport(List<ImportProductRequest> request) {
        FreeSlotsUtil.check(request.size(), productRepository, maxSlotsInStorage);
        User currentUser = userRepository.getCurrentUser();
        List<Product> productsForImport = new ArrayList<>();
        request.stream()
                .map(productRequest -> modelMapper.map(productRequest, Product.class))
                .forEach(product -> {
                    product.setImporterId(currentUser.getId());
                    productsForImport.add(product);
                });
        return productRepository.saveAll(productsForImport);
    }

    @Override
    public List<Product> addProductsForExport(ExportProductRequest request) {
        User currentUser = userRepository.getCurrentUser();
        List<Product> productsForExport = productRepository.findAllById(request.getProductsIds());
        productsForExport.stream()
                .filter(product -> product.getExporterId() == null)
                .forEach(product -> {
                    product.setExporterId(currentUser.getId());
                    product.setForExport(Boolean.TRUE);
                });
        return productRepository.saveAll(productsForExport);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        if (id == null) {
            return null;
        }
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            log.error("IN getById — no product found by id: {}", id);
        }
        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.getAllUniqueProducts(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getPendingExportProductsOfCurrentUser(Pageable pageable) {
        List<Product> productsForExport = productRepository.getProductsForExport(userRepository.getCurrentUser().getId());
        return new PageImpl<>(productsForExport, pageable, productsForExport.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getNewProductsForImport(Pageable pageable) {
        return productRepository.findByIsAvailableFalseAndForExportFalseAndIsExportedFalse(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsForExport(Pageable pageable) {
        return productRepository.findByIsAvailableTrueAndForExportTrueAndIsExportedFalse(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsWithFilter(String name, String manufacturer, LocalDate expiredAt, Integer page, Integer size, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);

        List<Predicate> predicates = fillPredicates(builder, root, name, manufacturer, expiredAt);
        countWithCriteria(builder, criteriaQuery, name, manufacturer, expiredAt);
        Order order = builder.desc(root.get("importedAt"));

        criteriaQuery.select(root)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(order);

        return createQueryWithPagination(criteriaQuery, page, size, pageable);
    }

    private List<Predicate> fillPredicates(CriteriaBuilder builder, Root<Product> root, String name, String manufacturer, LocalDate expiredAt) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.isTrue(root.get("isAvailable")));
        predicates.add(builder.isNotNull(root.get("importedAt")));
        if (name != null) {
            predicates.add(builder.like(root.get("name"), name + "%"));
        }
        if (manufacturer != null) {
            predicates.add(builder.like(root.get("manufacturer"), manufacturer + "%"));
        }
        if (expiredAt != null) {
            predicates.add(builder.equal(root.get("expiredAt"), expiredAt));
        }
        return predicates;
    }

    private void countWithCriteria(CriteriaBuilder builder, CriteriaQuery<Product> criteriaQuery, String name, String manufacturer, LocalDate expiredAt) {
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Product> rootCount = countCriteria.from(criteriaQuery.getResultType());
        countCriteria.select(builder.count(rootCount));
        List<Predicate> predicates = fillPredicates(builder, rootCount, name, manufacturer, expiredAt);
        countCriteria.where(predicates.toArray(new Predicate[]{}));
        entityManager.createQuery(countCriteria).getSingleResult();
    }

    private Page<Product> createQueryWithPagination(CriteriaQuery<Product> criteriaQuery, Integer page, Integer size, Pageable pageable) {
        TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery);
        int totalItems = typedQuery.getResultList().size();
        if (page == null && size == null) {
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        if (page != null && size == null) {
            typedQuery.setFirstResult(page * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        if (page == null && size != null) {
            typedQuery.setFirstResult(pageable.getPageNumber() * size);
            typedQuery.setMaxResults(size);
        }
        if (page != null && size != null) {
            typedQuery.setFirstResult(page * size);
            typedQuery.setMaxResults(size);
        }
        return new PageImpl<>(typedQuery.getResultList(), pageable, totalItems);
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
