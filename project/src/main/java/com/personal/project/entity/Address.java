package com.personal.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @NotBlank(message = "Street cannot be blank")
    @Column(nullable = false)
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Column(nullable = false)
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @NotBlank(message = "Country cannot be blank")
    @Column(nullable = false)
    private String country;
}