package com.personal.project;

import com.personal.project.dtos.ConvertVirtualToPhysicalRequestDto;
import com.personal.project.dtos.ConvertVirtualToPhysicalResponseDto;
import com.personal.project.entity.*;
import com.personal.project.exceptions.InsufficientGoldQuantityException;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.PhysicalGoldTransactionMapper;
import com.personal.project.repositories.PhysicalGoldTransactionRepository;
import com.personal.project.repositories.TransactionHistoryRepository;
import com.personal.project.repositories.UserRepository;
import com.personal.project.repositories.VendorBranchRepository;
import com.personal.project.repositories.VendorRepository;
import com.personal.project.repositories.VirtualGoldHoldingRepository;
import com.personal.project.servicesImpl.PhysicalGoldTransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhysicalGoldTransactionServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private VendorBranchRepository vendorBranchRepository;

    @Mock
    private VirtualGoldHoldingRepository
            virtualGoldHoldingRepository;

    @Mock
    private PhysicalGoldTransactionRepository
            physicalGoldTransactionRepository;

    @Mock
    private TransactionHistoryRepository
            transactionHistoryRepository;

    @Mock
    private PhysicalGoldTransactionMapper
            physicalGoldTransactionMapper;

    @InjectMocks
    private PhysicalGoldTransactionServiceImpl
            physicalGoldTransactionService;

    private User user;

    private Vendor vendor;

    private VendorBranch vendorBranch;

    private VirtualGoldHolding holding;

    private PhysicalGoldTransaction
            physicalGoldTransaction;

    private ConvertVirtualToPhysicalResponseDto
            responseDto;

    private Address address;

    @BeforeEach
    void setUp() {

        address =
                Address.builder()
                        .postalCode("160062")
                        .build();

        user = User.builder()
                .userId(1)
                .name("Swarit Sharma")
                .address(address)
                .build();

        vendor = Vendor.builder()
                .vendorId(1)
                .vendorName("Tanishq")
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
                .quantity(
                        new BigDecimal("100")
                )
                .build();

        holding = VirtualGoldHolding.builder()
                .holdingId(1)
                .user(user)
                .branch(vendorBranch)
                .quantity(
                        new BigDecimal("10")
                )
                .build();

        physicalGoldTransaction =
                PhysicalGoldTransaction.builder()
                        .transactionId(1)
                        .user(user)
                        .branch(vendorBranch)
                        .quantity(
                                new BigDecimal("2")
                        )
                        .deliveryAddress(address)
                        .build();

        responseDto =
                ConvertVirtualToPhysicalResponseDto
                        .builder()
                        .transactionId(1)
                        .quantity("2")
                        .transactionStatus(
                                "SUCCESS"
                        )
                        .build();
    }

    @Test
    void convertVirtualToPhysical_ShouldConvertSuccessfully() {

        ConvertVirtualToPhysicalRequestDto dto =
                ConvertVirtualToPhysicalRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("2")
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(vendorRepository.findById(1))
                .thenReturn(Optional.of(vendor));

        when(vendorBranchRepository
                .findFirstByVendorVendorIdAndAddressPostalCodeOrderByQuantityDesc(
                        1,
                        "160062"
                ))
                .thenReturn(
                        Optional.of(vendorBranch)
                );

        when(virtualGoldHoldingRepository
                .findByUserUserIdAndBranchBranchId(
                        1,
                        1
                ))
                .thenReturn(Optional.of(holding));

        when(virtualGoldHoldingRepository
                .save(any(
                        VirtualGoldHolding.class
                )))
                .thenReturn(holding);

        when(physicalGoldTransactionRepository
                .save(any(
                        PhysicalGoldTransaction.class
                )))
                .thenReturn(
                        physicalGoldTransaction
                );

        when(physicalGoldTransactionMapper
                .toConvertVirtualToPhysicalResponseDto(
                        physicalGoldTransaction
                ))
                .thenReturn(responseDto);

        ConvertVirtualToPhysicalResponseDto
                result =
                physicalGoldTransactionService
                        .convertVirtualToPhysical(
                                1,
                                dto
                        );

        assertNotNull(result);

        verify(
                transactionHistoryRepository,
                times(1)
        ).save(any(TransactionHistory.class));
    }

    @Test
    void convertVirtualToPhysical_ShouldThrowException_WhenUserNotFound() {

        ConvertVirtualToPhysicalRequestDto dto =
                ConvertVirtualToPhysicalRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("2")
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> physicalGoldTransactionService
                        .convertVirtualToPhysical(
                                1,
                                dto
                        )
        );
    }

    @Test
    void convertVirtualToPhysical_ShouldThrowException_WhenVendorNotFound() {

        ConvertVirtualToPhysicalRequestDto dto =
                ConvertVirtualToPhysicalRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("2")
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(vendorRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> physicalGoldTransactionService
                        .convertVirtualToPhysical(
                                1,
                                dto
                        )
        );
    }

    @Test
    void convertVirtualToPhysical_ShouldThrowException_WhenHoldingNotFound() {

        ConvertVirtualToPhysicalRequestDto dto =
                ConvertVirtualToPhysicalRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("2")
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(vendorRepository.findById(1))
                .thenReturn(Optional.of(vendor));

        when(vendorBranchRepository
                .findFirstByVendorVendorIdAndAddressPostalCodeOrderByQuantityDesc(
                        1,
                        "160062"
                ))
                .thenReturn(
                        Optional.of(vendorBranch)
                );

        when(virtualGoldHoldingRepository
                .findByUserUserIdAndBranchBranchId(
                        1,
                        1
                ))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> physicalGoldTransactionService
                        .convertVirtualToPhysical(
                                1,
                                dto
                        )
        );
    }

    @Test
    void convertVirtualToPhysical_ShouldThrowException_WhenInsufficientGoldQuantity() {

        holding.setQuantity(
                new BigDecimal("1")
        );

        ConvertVirtualToPhysicalRequestDto dto =
                ConvertVirtualToPhysicalRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("2")
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(vendorRepository.findById(1))
                .thenReturn(Optional.of(vendor));

        when(vendorBranchRepository
                .findFirstByVendorVendorIdAndAddressPostalCodeOrderByQuantityDesc(
                        1,
                        "160062"
                ))
                .thenReturn(
                        Optional.of(vendorBranch)
                );

        when(virtualGoldHoldingRepository
                .findByUserUserIdAndBranchBranchId(
                        1,
                        1
                ))
                .thenReturn(Optional.of(holding));

        assertThrows(
                InsufficientGoldQuantityException.class,
                () -> physicalGoldTransactionService
                        .convertVirtualToPhysical(
                                1,
                                dto
                        )
        );
    }
}