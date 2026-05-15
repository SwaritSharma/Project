package com.personal.project;

import com.personal.project.dtos.BuyVirtualGoldRequestDto;
import com.personal.project.dtos.SellVirtualGoldRequestDto;
import com.personal.project.dtos.VirtualGoldHoldingResponseDto;
import com.personal.project.entity.*;
import com.personal.project.exceptions.InsufficientBalanceException;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.VirtualGoldHoldingMapper;
import com.personal.project.repositories.TransactionHistoryRepository;
import com.personal.project.repositories.UserRepository;
import com.personal.project.repositories.VendorBranchRepository;
import com.personal.project.repositories.VendorRepository;
import com.personal.project.repositories.VirtualGoldHoldingRepository;
import com.personal.project.servicesImpl.VirtualGoldHoldingServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VirtualGoldHoldingServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private VendorBranchRepository vendorBranchRepository;

    @Mock
    private VirtualGoldHoldingRepository virtualGoldHoldingRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private VirtualGoldHoldingMapper virtualGoldHoldingMapper;

    @InjectMocks
    private VirtualGoldHoldingServiceImpl virtualGoldHoldingService;

    private User user;

    private Vendor vendor;

    private VendorBranch vendorBranch;

    private VirtualGoldHolding holding;

    private VirtualGoldHoldingResponseDto responseDto;

    @BeforeEach
    void setUp() {

        Address address =
                Address.builder()
                        .postalCode("160062")
                        .build();

        user = User.builder()
                .userId(1)
                .name("Swarit Sharma")
                .balance(
                        new BigDecimal("10000")
                )
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
                        new BigDecimal("1")
                )
                .build();

        responseDto =
                VirtualGoldHoldingResponseDto
                        .builder()
                        .holdingId(1)
                        .quantity("1")
                        .build();
    }

    @Test
    void buyVirtualGold_ShouldBuySuccessfully() {

        BuyVirtualGoldRequestDto dto =
                BuyVirtualGoldRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("1")
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(vendorRepository.findById(1))
                .thenReturn(Optional.of(vendor));

        when(vendorBranchRepository
                .findFirstByVendorVendorIdAndAddressPostalCodeAndQuantityGreaterThanEqualOrderByQuantityDesc(
                        1,
                        "160062",
                        new BigDecimal("1")
                ))
                .thenReturn(Optional.of(vendorBranch));

        when(virtualGoldHoldingRepository
                .findByUserUserIdAndBranchBranchId(
                        anyInt(),
                        anyInt()
                ))
                .thenReturn(Optional.empty());

        when(vendorBranchRepository.save(any(
                VendorBranch.class
        )))
                .thenReturn(vendorBranch);

        when(vendorRepository.save(any(
                Vendor.class
        )))
                .thenReturn(vendor);

        when(userRepository.save(any(
                User.class
        )))
                .thenReturn(user);

        when(virtualGoldHoldingRepository
                .save(any(
                        VirtualGoldHolding.class
                )))
                .thenReturn(holding);

        when(virtualGoldHoldingMapper
                .toVirtualGoldHoldingResponseDto(
                        holding
                ))
                .thenReturn(responseDto);

        VirtualGoldHoldingResponseDto result =
                virtualGoldHoldingService
                        .buyVirtualGold(
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
    void buyVirtualGold_ShouldThrowException_WhenBalanceInsufficient() {

        user.setBalance(
                new BigDecimal("100")
        );

        BuyVirtualGoldRequestDto dto =
                BuyVirtualGoldRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("1")
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(vendorRepository.findById(1))
                .thenReturn(Optional.of(vendor));

        when(vendorBranchRepository
                .findFirstByVendorVendorIdAndAddressPostalCodeAndQuantityGreaterThanEqualOrderByQuantityDesc(
                        1,
                        "160062",
                        new BigDecimal("1")
                ))
                .thenReturn(Optional.of(vendorBranch));

        assertThrows(
                InsufficientBalanceException.class,
                () -> virtualGoldHoldingService
                        .buyVirtualGold(
                                1,
                                dto
                        )
        );
    }

    @Test
    void buyVirtualGold_ShouldThrowException_WhenVendorNotFound() {

        BuyVirtualGoldRequestDto dto =
                BuyVirtualGoldRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("1")
                        )
                        .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(vendorRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> virtualGoldHoldingService
                        .buyVirtualGold(
                                1,
                                dto
                        )
        );
    }

    @Test
    void sellVirtualGold_ShouldSellSuccessfully() {

        SellVirtualGoldRequestDto dto =
                SellVirtualGoldRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("0.5")
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
                .thenReturn(Optional.of(vendorBranch));

        when(virtualGoldHoldingRepository
                .findByUserUserIdAndBranchBranchId(
                        anyInt(),
                        anyInt()
                ))
                .thenReturn(Optional.of(holding));

        when(vendorBranchRepository.save(any(
                VendorBranch.class
        )))
                .thenReturn(vendorBranch);

        when(vendorRepository.save(any(
                Vendor.class
        )))
                .thenReturn(vendor);

        when(userRepository.save(any(
                User.class
        )))
                .thenReturn(user);

        when(virtualGoldHoldingMapper
                .toVirtualGoldHoldingResponseDto(
                        holding
                ))
                .thenReturn(responseDto);

        VirtualGoldHoldingResponseDto result =
                virtualGoldHoldingService
                        .sellVirtualGold(
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
    void sellVirtualGold_ShouldThrowException_WhenHoldingNotFound() {

        SellVirtualGoldRequestDto dto =
                SellVirtualGoldRequestDto
                        .builder()
                        .vendorId(1)
                        .quantity(
                                new BigDecimal("1")
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
                .thenReturn(Optional.of(vendorBranch));

        when(virtualGoldHoldingRepository
                .findByUserUserIdAndBranchBranchId(
                        anyInt(),
                        anyInt()
                ))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> virtualGoldHoldingService
                        .sellVirtualGold(
                                1,
                                dto
                        )
        );
    }
}