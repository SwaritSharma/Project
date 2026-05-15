package com.personal.project.entity;

import com.personal.project.enums.TransactionStatus;
import com.personal.project.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private VendorBranch branch;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(
            value = "0.01",
            message = "Quantity must be greater than 0"
    )
    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal quantity;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(
            value = "0.01",
            message = "Amount must be greater than 0"
    )
    @Column(
            nullable = false,
            precision = 18,
            scale = 2
    )
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}