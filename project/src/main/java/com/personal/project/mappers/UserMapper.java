package com.personal.project.mappers;

import com.personal.project.dtos.*;
import com.personal.project.entity.Address;
import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(
            target = "address",
            expression = "java(mapAddress(dto))"
    )
    User toUser(
            UserRegistrationRequestDto dto
    );

    @Mapping(
            target = "street",
            source = "address.street"
    )
    @Mapping(
            target = "city",
            source = "address.city"
    )
    @Mapping(
            target = "state",
            source = "address.state"
    )
    @Mapping(
            target = "postalCode",
            source = "address.postalCode"
    )
    @Mapping(
            target = "country",
            source = "address.country"
    )
    UserDashboardResponseDto
    toUserDashboardResponseDto(
            User user
    );

    List<LatestTransactionDto>
    toLatestTransactionDtos(
            List<TransactionHistory> transactions
    );

    @Mapping(
            target = "vendorName",
            source = "branch.vendor.vendorName"
    )
    @Mapping(
            target = "branchCity",
            source = "branch.address.city"
    )
    @Mapping(
            target = "branchState",
            source = "branch.address.state"
    )
    LatestTransactionDto
    toLatestTransactionDto(
            TransactionHistory transactionHistory
    );

    default Address mapAddress(
            UserRegistrationRequestDto dto
    ) {

        return Address.builder()
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .build();
    }
}