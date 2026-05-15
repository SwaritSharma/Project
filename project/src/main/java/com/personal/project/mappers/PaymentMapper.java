package com.personal.project.mappers;

import com.personal.project.dtos.PaymentResponseDto;
import com.personal.project.dtos.TransactionHistoryResponseDto;
import com.personal.project.entity.Payment;
import com.personal.project.entity.TransactionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponseDto
    toPaymentResponseDto(
            Payment payment
    );

    List<PaymentResponseDto>
    toPaymentResponseDtos(
            List<Payment> payments
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
    TransactionHistoryResponseDto
    toTransactionHistoryResponseDto(
            TransactionHistory transactionHistory
    );

    List<TransactionHistoryResponseDto>
    toTransactionHistoryResponseDtos(
            List<TransactionHistory> transactionHistories
    );
}