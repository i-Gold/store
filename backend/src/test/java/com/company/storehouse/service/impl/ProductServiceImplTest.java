package com.company.storehouse.service.impl;

import com.company.storehouse.controller.exception.FreeSlotsLimitException;
import com.company.storehouse.controller.request.ImportProductRequest;
import com.company.storehouse.model.Authority;
import com.company.storehouse.model.Product;
import com.company.storehouse.model.Role;
import com.company.storehouse.model.User;
import com.company.storehouse.model.enumeration.AuthorityName;
import com.company.storehouse.model.enumeration.RoleName;
import com.company.storehouse.repository.ProductRepository;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.repository.WaybillRepository;
import com.company.storehouse.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceImplTest {

    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 20;
    private static final int TOTAL_PAGES = 1;
    private static final int TOTAL_ELEMENTS = 3;
    private static final Long TEST_ID = 3L;
    private static final String TEST_MANUFACTURER = "CANON";
    private User testUser;
    private Product testProduct;
    private ImportProductRequest forImportTest;
    private List<ImportProductRequest> testImportRequest = new ArrayList<>();
    private List<Product> testProductList = new ArrayList<>();

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WaybillRepository waybillRepository;
    @InjectMocks
    private ProductService productService = new ProductServiceImpl();

    @BeforeEach
    public void setup() {
        testUser = getTestUser();
        testProduct = getTestProduct().orElse(null);

        forImportTest = loadData();
        when(modelMapper.map(forImportTest, Product.class)).thenReturn(testProduct);

        String maxSlotsInStorage = StringUtils.EMPTY;
        ReflectionTestUtils.setField(productService, "maxSlotsInStorage", "500", maxSlotsInStorage.getClass());
        when(productRepository.countAllProductsInStock()).thenReturn(BigInteger.valueOf(497L));

        when(productRepository.getAllUniqueProducts(PageRequest.of(PAGE, PAGE_SIZE))).thenReturn(getListOfProducts());
        when(productRepository.findById(TEST_ID)).thenReturn(Optional.of(testProduct));
        when(userRepository.getCurrentUser()).thenReturn(testUser);
        when(productRepository.saveAll(testProductList)).thenReturn(testProductList);
    }

    @Test
    public void shouldCreateProductsForImport() {
        List<Product> actual = productService.addProductsForImport(testImportRequest);
        verify(userRepository).getCurrentUser();
        verify(modelMapper, times(3)).map(forImportTest, Product.class);
        verify(productRepository).saveAll(testProductList);
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(3).isEqualTo(actual.size());
        Assertions.assertThat(testProductList).isEqualTo(actual);
        Assertions.assertThat(actual.get(0).getImporterId()).isNotNull();
        Assertions.assertThat(actual.get(1).getImporterId()).isNotNull();
        Assertions.assertThat(actual.get(2).getImporterId()).isNotNull();
        Assertions.assertThat(Long.valueOf(1L)).isEqualTo(actual.get(0).getImporterId());
        Assertions.assertThat(Long.valueOf(1L)).isEqualTo(actual.get(0).getImporterId());
        Assertions.assertThat(Long.valueOf(1L)).isEqualTo(actual.get(0).getImporterId());
    }

    @Test
    public void shouldFailCreateProductsForImport() {
        String maxSlotsInStorage = StringUtils.EMPTY;
        ReflectionTestUtils.setField(productService, "maxSlotsInStorage", "500", maxSlotsInStorage.getClass());
        when(productRepository.countAllProductsInStock()).thenReturn(BigInteger.valueOf(500L));

        ThrowableAssert.ThrowingCallable throwingCallable = () -> productService.addProductsForImport(testImportRequest);
        Assertions.assertThatExceptionOfType(FreeSlotsLimitException.class).isThrownBy(throwingCallable);
    }

    @Test
    public void shouldFindProductById() {
        Product actual = productService.getById(TEST_ID);
        verify(productRepository).findById(TEST_ID);
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(TEST_MANUFACTURER).isEqualTo(actual.getManufacturer());
    }

    @Test
    public void shouldFailFindProductById() {
        when(productRepository.findById(TEST_ID)).thenReturn(null);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> productService.getById(TEST_ID);
        Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(throwingCallable);
    }

    @Test
    public void shouldGetAllProducts() {
        Page<Product> actual = productService.getAllProducts(PageRequest.of(PAGE, PAGE_SIZE));
        verify(productRepository).getAllUniqueProducts(PageRequest.of(PAGE, PAGE_SIZE));
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(PAGE).isEqualTo(actual.getNumber());
        Assertions.assertThat(TOTAL_PAGES).isEqualTo(actual.getTotalPages());
        Assertions.assertThat(TOTAL_ELEMENTS).isEqualTo(actual.getTotalElements());
        Assertions.assertThat(TEST_ID).isEqualTo(actual.getContent().get(0).getId());
    }

    private ImportProductRequest loadData() {
        ImportProductRequest forImportTest = new ImportProductRequest();
        forImportTest.setManufacturer(testProduct.getManufacturer());
        forImportTest.setName(testProduct.getName());
        forImportTest.setProducedAt(testProduct.getProducedAt());
        forImportTest.setExpiredAt(testProduct.getExpiredAt());
        forImportTest.setWeight(testProduct.getWeight());

        testImportRequest.add(forImportTest);
        testImportRequest.add(forImportTest);
        testImportRequest.add(forImportTest);

        testProductList.add(testProduct);
        testProductList.add(testProduct);
        testProductList.add(testProduct);

        return forImportTest;
    }

    private Optional<Product> getTestProduct() {
        Product product = new Product();
        product.setId(TEST_ID);
        product.setName("DMC-G7");
        product.setManufacturer("PANASONIC");
        product.setWeight(13.0d);
        product.setProducedAt(LocalDate.of(2019, Month.APRIL, 20));
        product.setExpiredAt(LocalDate.of(2020, Month.APRIL, 20));
        product.setImportedAt(LocalDateTime.of(2020, Month.MAY, 20, 12, 0));
        product.setIsAvailable(Boolean.TRUE);
        return Optional.of(product);
    }

    private Page<Product> getListOfProducts() {
        Product first = testProduct;

        Product second = testProduct;
        second.setId(2L);
        second.setName("D3500");
        second.setManufacturer("NIKON");

        Product third = testProduct;
        third.setId(3L);
        third.setName("EOS 77 D");
        third.setManufacturer("CANON");

        List<Product> result = Stream.of(first, second, third)
                .sorted(Comparator.comparing(Product::getImportedAt).reversed())
                .collect(Collectors.toList());
        return new PageImpl<>(result, PageRequest.of(PAGE, PAGE_SIZE), result.size());
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);

        Authority authority = new Authority();
        authority.setName(AuthorityName.APPROVE_ON_IMPORT_EXPORT);
        Role role = new Role(RoleName.MANAGER);
        role.setAuthorities(Collections.singleton(authority));

        user.setRole(role);
        return user;
    }

}
