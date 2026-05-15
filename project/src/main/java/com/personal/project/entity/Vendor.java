package com.personal.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Integer vendorId;

    @NotBlank(message = "Vendor name cannot be blank")
    @Column(name = "vendor_name", nullable = false, unique = true)
    private String vendorName;

    private String description;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Email(message = "Invalid email format")
    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "website_url")
    private String websiteUrl;

    @DecimalMin(value = "0.0")
    @Column(
            name = "total_gold_quantity",
            nullable = false,
            precision = 18,
            scale = 2
    )
    private BigDecimal totalGoldQuantity;

    @DecimalMin(value = "0.0")
    @Column(
            name = "current_gold_price",
            nullable = false,
            precision = 18,
            scale = 2
    )
    private BigDecimal currentGoldPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotBlank(message = "Password cannot be blank")
    @Column(nullable = false)
    private String password;

    @OneToMany(
            mappedBy = "vendor",
            cascade = CascadeType.ALL
    )
    private List<VendorBranch> branches;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}