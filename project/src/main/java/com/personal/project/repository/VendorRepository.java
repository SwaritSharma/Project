package com.personal.project.repository;

import com.personal.project.entity.Vendor;
import com.personal.project.projection.VendorDashboardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(
        path = "vendors",
        excerptProjection =
                VendorDashboardProjection.class
)
public interface VendorRepository
        extends JpaRepository<Vendor, Integer> {

    boolean existsByVendorName(
            String vendorName
    );

    Optional<Vendor> findByVendorName(
            String vendorName
    );
}