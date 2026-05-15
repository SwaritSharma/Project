package com.personal.project;

import com.personal.project.dtos.VendorBranchResponseDto;
import com.personal.project.dtos.VendorDashboardResponseDto;
import com.personal.project.entity.Address;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.VendorMapper;
import com.personal.project.repositories.TransactionHistoryRepository;
import com.personal.project.repositories.VendorBranchRepository;
import com.personal.project.repositories.VendorRepository;
import com.personal.project.servicesImpl.VendorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceImplTest {

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private VendorBranchRepository
            vendorBranchRepository;

    @Mock
    private TransactionHistoryRepository
            transactionHistoryRepository;

    @Mock
    private VendorMapper vendorMapper;

    @InjectMocks
    private VendorServiceImpl vendorService;

    private Vendor vendor;

    private VendorBranch vendorBranch;

    private VendorDashboardResponseDto
            vendorDashboardResponseDto;

    private VendorBranchResponseDto
            vendorBranchResponseDto;

    @BeforeEach
    void setUp() {

        Address address =
                Address.builder()
                        .street("Street 1")
                        .city("Mohali")
                        .state("Punjab")
                        .postalCode("160062")
                        .country("India")
                        .build();

        vendor = Vendor.builder()
                .vendorId(1)
                .vendorName("Tanishq")
                .description("Gold Vendor")
                .websiteUrl("www.tanishq.com")
                .currentGoldPrice(
                        new BigDecimal("7000")
                )
                .totalGoldQuantity(
                        new BigDecimal("1000")
                )
                .build();

        vendorBranch = VendorBranch.builder()
                .branchId(1)
                .vendor(vendor)
                .address(address)
                .quantity(
                        new BigDecimal("500")
                )
                .build();

        vendorDashboardResponseDto =
                VendorDashboardResponseDto.builder()
                        .vendorId(1)
                        .vendorName("Tanishq")
                        .description("Gold Vendor")
                        .websiteUrl(
                                "www.tanishq.com"
                        )
                        .currentGoldPrice(
                                new BigDecimal("7000")
                        )
                        .totalGoldQuantity(
                                new BigDecimal("1000")
                        )
                        .build();

        vendorBranchResponseDto =
                VendorBranchResponseDto.builder()
                        .branchId(1)
                        .street("Street 1")
                        .city("Mohali")
                        .state("Punjab")
                        .postalCode("160062")
                        .country("India")
                        .quantity(
                                new BigDecimal("500")
                        )
                        .build();
    }

    @Test
    void getAllVendors_ShouldReturnVendorsSuccessfully() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Vendor> vendorPage =
                new PageImpl<>(
                        Collections.singletonList(
                                vendor
                        )
                );

        when(vendorRepository.findAll(pageable))
                .thenReturn(vendorPage);

        when(vendorMapper
                .toVendorDashboardResponseDto(
                        vendor
                ))
                .thenReturn(
                        vendorDashboardResponseDto
                );

        Page<VendorDashboardResponseDto>
                result =
                vendorService.getAllVendors(
                        pageable
                );

        assertEquals(
                1,
                result.getTotalElements()
        );
    }

    @Test
    void getVendorById_ShouldReturnVendorSuccessfully() {

        when(vendorRepository.findById(1))
                .thenReturn(Optional.of(vendor));

        when(vendorMapper
                .toVendorDashboardResponseDto(
                        vendor
                ))
                .thenReturn(
                        vendorDashboardResponseDto
                );

        VendorDashboardResponseDto result =
                vendorService.getVendorById(1);

        assertNotNull(result);

        assertEquals(
                "Tanishq",
                result.getVendorName()
        );
    }

    @Test
    void getVendorById_ShouldThrowException_WhenVendorNotFound() {

        when(vendorRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> vendorService.getVendorById(1)
        );
    }

    @Test
    void getVendorBranches_ShouldReturnBranchesSuccessfully() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<VendorBranch> branchPage =
                new PageImpl<>(
                        Collections.singletonList(
                                vendorBranch
                        )
                );

        when(vendorBranchRepository
                .findByVendorVendorId(
                        1,
                        pageable
                ))
                .thenReturn(branchPage);

        when(vendorMapper
                .toVendorBranchResponseDto(
                        vendorBranch
                ))
                .thenReturn(
                        vendorBranchResponseDto
                );

        Page<VendorBranchResponseDto>
                result =
                vendorService.getVendorBranches(
                        1,
                        pageable
                );

        assertEquals(
                1,
                result.getTotalElements()
        );
    }

    @Test
    void getVendorBranchesByState_ShouldReturnBranchesSuccessfully() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<VendorBranch> branchPage =
                new PageImpl<>(
                        Collections.singletonList(
                                vendorBranch
                        )
                );

        when(vendorBranchRepository
                .findByVendorVendorIdAndAddressState(
                        1,
                        "Punjab",
                        pageable
                ))
                .thenReturn(branchPage);

        when(vendorMapper
                .toVendorBranchResponseDto(
                        vendorBranch
                ))
                .thenReturn(
                        vendorBranchResponseDto
                );

        Page<VendorBranchResponseDto>
                result =
                vendorService
                        .getVendorBranchesByState(
                                1,
                                "Punjab",
                                pageable
                        );

        assertEquals(
                1,
                result.getTotalElements()
        );
    }

    @Test
    void getVendorBranchesSortedByQuantityDesc_ShouldReturnBranchesSuccessfully() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<VendorBranch> branchPage =
                new PageImpl<>(
                        Collections.singletonList(
                                vendorBranch
                        )
                );

        when(vendorBranchRepository
                .findByVendorVendorIdOrderByQuantityDesc(
                        1,
                        pageable
                ))
                .thenReturn(branchPage);

        when(vendorMapper
                .toVendorBranchResponseDto(
                        vendorBranch
                ))
                .thenReturn(
                        vendorBranchResponseDto
                );

        Page<VendorBranchResponseDto>
                result =
                vendorService
                        .getVendorBranchesSortedByQuantityDesc(
                                1,
                                pageable
                        );

        assertEquals(
                1,
                result.getTotalElements()
        );
    }
}