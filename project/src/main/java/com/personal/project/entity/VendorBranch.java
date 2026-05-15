package com.personal.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendor_branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Integer branchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @DecimalMin(value = "0.0")
    @Column(
            nullable = false,
            precision = 18,
            scale = 2
    )
    private BigDecimal quantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "branch")
    private List<TransactionHistory> transactions;

    @OneToMany(mappedBy = "branch")
    private List<VirtualGoldHolding> holdings;

    @OneToMany(mappedBy = "branch")
    private List<PhysicalGoldTransaction> physicalGoldTransactions;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}