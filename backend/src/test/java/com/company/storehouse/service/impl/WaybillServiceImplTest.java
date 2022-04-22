package com.company.storehouse.service.impl;

import com.company.storehouse.controller.exception.ApproveWaybillException;
import com.company.storehouse.controller.exception.ProductsNotFoundException;
import com.company.storehouse.model.Authority;
import com.company.storehouse.model.Product;
import com.company.storehouse.model.Role;
import com.company.storehouse.model.User;
import com.company.storehouse.model.Waybill;
import com.company.storehouse.model.enumeration.AuthorityName;
import com.company.storehouse.model.enumeration.ProcessingStatus;
import com.company.storehouse.model.enumeration.RoleName;
import com.company.storehouse.model.enumeration.WaybillType;
import com.company.storehouse.repository.ProductRepository;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.repository.WaybillRepository;
import com.company.storehouse.service.WaybillService;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WaybillServiceImplTest {

    private static final Long TEST_ID = 1L;
    private User testUser;
    private Waybill expected;

    @Mock
    private UserRepository userRepository;
    @Mock
    private WaybillRepository waybillRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private WaybillService waybillService = new WaybillServiceImpl();

    @BeforeEach
    public void setup() {
        testUser = getTestUser();
        expected = getTestWaybill();

        Waybill input = getTestInputWaybill();
        when(productRepository.countAllProductsInStock()).thenReturn(BigInteger.valueOf(497L));
        when(waybillRepository.save(expected)).thenReturn(expected);
        when(userRepository.getCurrentUser()).thenReturn(testUser);
        when(productRepository.getProductsForImport(testUser.getId())).thenReturn(getListOfProducts());
        when(waybillRepository.findById(TEST_ID)).thenReturn(Optional.of(input));
    }

    @Test
    public void shouldCreateWaybillForImport() {
        String maxSlotsInStorage = StringUtils.EMPTY;
        ReflectionTestUtils.setField(waybillService, "maxSlotsInStorage", "500", maxSlotsInStorage.getClass());

        Waybill actual = waybillService.createWaybillForImport();
        verify(productRepository).getProductsForImport(testUser.getId());
        verify(waybillRepository).save(expected);
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(expected).isEqualTo(actual);
        Assertions.assertThat(actual.getProducts()).isNotNull();
    }

    @Test
    public void shouldFailCreationWaybillForImport() {
        when(productRepository.getProductsForImport(testUser.getId())).thenReturn(Collections.emptyList());
        ThrowableAssert.ThrowingCallable throwingCallable = () -> waybillService.createWaybillForImport();
        Assertions.assertThatExceptionOfType(ProductsNotFoundException.class).isThrownBy(throwingCallable);
    }

    @Test
    public void shouldApproveOrRejectWaybill() {
        String maxSlotsInStorage = StringUtils.EMPTY;
        ReflectionTestUtils.setField(waybillService, "maxSlotsInStorage", "500", maxSlotsInStorage.getClass());

        when(waybillRepository.findById(TEST_ID)).thenReturn(Optional.of(expected));
        waybillService.approveOrRejectWaybill(TEST_ID, ProcessingStatus.COMPLETED);
        verify(waybillRepository).findById(TEST_ID);
        verify(userRepository).getCurrentUser();
        verify(waybillRepository).save(expected);
    }

    @Test
    public void shouldFailApproveOrRejectWaybillWithWrongStatus() {
        ThrowableAssert.ThrowingCallable throwingCallable = () -> waybillService.approveOrRejectWaybill(TEST_ID, ProcessingStatus.PENDING);
        Assertions.assertThatExceptionOfType(ApproveWaybillException.class).isThrownBy(throwingCallable);
    }

    @Test
    public void shouldFailApproveOrRejectWaybillAlreadyApproved() {
        Waybill failed = getTestWaybill();
        failed.setId(2L);
        failed.setIsApproved(Boolean.TRUE);
        when(waybillRepository.findById(failed.getId())).thenReturn(Optional.of(failed));
        ThrowableAssert.ThrowingCallable throwingCallable = () -> waybillService.approveOrRejectWaybill(failed.getId(), ProcessingStatus.COMPLETED);
        Assertions.assertThatExceptionOfType(ApproveWaybillException.class).isThrownBy(throwingCallable);
    }

    @Test
    public void shouldGetWaybillsForConfirmationImport() {
        PageRequest pageable = PageRequest.of(0, 20);
        waybillService.getWaybillsForConfirmationImport(pageable);
        verify(waybillRepository).findAllByStatusAndTypeAndIsApprovedFalseAndCreatedAtNull(ProcessingStatus.PENDING, WaybillType.INPUT, pageable);
    }

    private User getTestUser() {
        User user = new User();
        user.setId(TEST_ID);

        Authority authority = new Authority();
        authority.setName(AuthorityName.APPROVE_ON_IMPORT_EXPORT);
        Role role = new Role(RoleName.MANAGER);
        role.setAuthorities(Collections.singleton(authority));

        user.setRole(role);
        return user;
    }

    private Waybill getTestWaybill() {
        Waybill result = new Waybill();
        result.setStatus(ProcessingStatus.PENDING);
        result.setType(WaybillType.INPUT);
        result.setOwnerId(testUser.getId());
        result.setProducts(getListOfProducts());
        return result;
    }

    private Waybill getTestInputWaybill() {
        Waybill result = new Waybill();
        result.setStatus(ProcessingStatus.PENDING);
        result.setType(WaybillType.INPUT);
        result.setOwnerId(testUser.getId());
        result.setProducts(getListOfProducts());
        return result;
    }

    private List<Product> getListOfProducts() {
        Product first = new Product();
        first.setId(TEST_ID);
        first.setName("EOS 77 D");
        first.setManufacturer("CANON");
        first.setWeight(10.0d);
        first.setProducedAt(LocalDate.of(2019, Month.APRIL, 20));
        first.setExpiredAt(LocalDate.of(2020, Month.APRIL, 20));
        first.setImportedAt(LocalDateTime.of(2019, Month.MAY, 20, 12, 0));
        first.setImporterId(TEST_ID);
        first.setIsPendingImport(Boolean.TRUE);
        first.setIsAvailable(Boolean.FALSE);

        Product second = new Product();
        second.setId(2L);
        second.setName("D3500");
        second.setManufacturer("NIKON");
        second.setWeight(12.0d);
        second.setProducedAt(LocalDate.of(2019, Month.APRIL, 20));
        second.setExpiredAt(LocalDate.of(2020, Month.APRIL, 20));
        second.setImportedAt(LocalDateTime.of(2019, Month.MAY, 20, 12, 0));
        second.setImporterId(TEST_ID);
        second.setIsPendingImport(Boolean.TRUE);
        second.setIsAvailable(Boolean.FALSE);

        Product third = new Product();
        third.setId(3L);
        third.setName("DMC-G7");
        third.setManufacturer("PANASONIC");
        third.setWeight(13.0d);
        third.setProducedAt(LocalDate.of(2019, Month.APRIL, 20));
        third.setExpiredAt(LocalDate.of(2020, Month.APRIL, 20));
        third.setImportedAt(LocalDateTime.of(2020, Month.MAY, 20, 12, 0));
        third.setImporterId(TEST_ID);
        third.setIsPendingImport(Boolean.TRUE);
        third.setIsAvailable(Boolean.FALSE);

        return Stream.of(first, second, third)
                .collect(Collectors.toList());
    }

}
