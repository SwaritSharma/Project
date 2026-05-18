package com.personal.project.service.impl;

import com.personal.project.service.BranchAllocationService;

import com.personal.project.entity.Address;
import com.personal.project.entity.VendorBranch;
import com.personal.project.exception.AddressNotFoundException;
import com.personal.project.exception.BranchAllocationException;
import com.personal.project.exception.InvalidQuantityException;
import com.personal.project.exception.VendorNotFoundException;
import com.personal.project.repository.AddressRepository;
import com.personal.project.repository.VendorBranchRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class BranchAllocationServiceImpl
        implements BranchAllocationService {

    private final
    VendorBranchRepository
            vendorBranchRepository;

    private final
    AddressRepository
            addressRepository;

    public BranchAllocationServiceImpl(
            VendorBranchRepository
                    vendorBranchRepository,
            AddressRepository
                    addressRepository
    ) {

        this.vendorBranchRepository =
                vendorBranchRepository;

        this.addressRepository =
                addressRepository;
    }

    @Override
    public VendorBranch allocateBranch(
            Integer vendorId,
            Integer deliveryAddressId,
            BigDecimal requiredQuantity
    ) {

        if (vendorId == null) {
            throw new VendorNotFoundException("Vendor id is required");
        }

        if (deliveryAddressId == null) {
            throw new AddressNotFoundException("Delivery address id is required");
        }

        if (requiredQuantity == null || requiredQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        Address deliveryAddress =
                addressRepository
                        .findById(
                                deliveryAddressId
                        )
                        .orElseThrow(
                                () ->
                                        new AddressNotFoundException(
                                                "Delivery address not found"
                                        )
                        );

        VendorBranch samePostalCodeBranch =
                vendorBranchRepository
                        .findFirstByVendorVendorIdAndAddressPostalCodeAndQuantityGreaterThanEqual(
                                vendorId,
                                deliveryAddress.getPostalCode(),
                                requiredQuantity
                        )
                        .orElse(null);

        if (samePostalCodeBranch != null) {

            return samePostalCodeBranch;
        }

        List<VendorBranch> branches =
                vendorBranchRepository
                        .findByVendorVendorIdAndQuantityGreaterThanEqual(
                                vendorId,
                                requiredQuantity
                        );

        if (branches.isEmpty()) {

            throw new BranchAllocationException(
                    "No branch found with sufficient inventory"
            );
        }

        VendorBranch sameCityBranch =
                branches.stream()
                        .filter(
                                branch ->
                                        branch.getAddress() != null
                                                && branch.getAddress().getCity() != null
                                                && branch.getAddress()
                                                .getCity()
                                                .equalsIgnoreCase(
                                                        deliveryAddress
                                                                .getCity()
                                                )
                        )
                        .findFirst()
                        .orElse(null);

        if (sameCityBranch != null) {

            return sameCityBranch;
        }

        VendorBranch sameStateBranch =
                branches.stream()
                        .filter(
                                branch ->
                                        branch.getAddress() != null
                                                && branch.getAddress().getState() != null
                                                && branch.getAddress()
                                                .getState()
                                                .equalsIgnoreCase(
                                                        deliveryAddress
                                                                .getState()
                                                )
                        )
                        .findFirst()
                        .orElse(null);

        if (sameStateBranch != null) {

            return sameStateBranch;
        }

        return branches.stream()
                .max(
                        Comparator.comparing(
                                VendorBranch::getQuantity
                        )
                )
                .orElseThrow(
                        () ->
                                new BranchAllocationException(
                                        "No suitable branch found"
                                )
                );
    }
}