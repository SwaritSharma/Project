package com.personal.project.mapper;

import com.personal.project.dto.PaymentDTO;
import com.personal.project.entity.Payment;
import com.personal.project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDTO toDto(Payment payment);

    List<PaymentDTO> toDtoList(List<Payment> payments);

    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "transactionType", source = "transactionType")
    @Mapping(target = "paymentStatus", source = "paymentStatus")
    @Mapping(target = "createdAt", source = "createdAt")
    Payment toEntity(
            User user,
            BigDecimal amount,
            String paymentMethod,
            String transactionType,
            String paymentStatus,
            LocalDateTime createdAt
    );
}
