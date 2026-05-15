package com.personal.project.repositories;

import com.personal.project.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository
        extends JpaRepository<Address, Integer> {
}