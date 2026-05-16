package com.personal.project.config;

import com.personal.project.entity.User;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.Address;
import com.personal.project.repository.UserRepository;
import com.personal.project.repository.VendorRepository;
import com.personal.project.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Ensure User exists
        String userEmail = "pradeep.kumar@example.in";
        if (userRepository.findByEmail(userEmail).isEmpty()) {
            Address userAddress = new Address();
            userAddress.setStreet("707 Banana Avenue");
            userAddress.setCity("Lucknow");
            userAddress.setState("Uttar Pradesh");
            userAddress.setPostalCode("226001");
            userAddress.setCountry("India");
            userAddress = addressRepository.save(userAddress);

            User user = new User();
            user.setName("Pradeep Kumar");
            user.setEmail(userEmail);
            user.setPassword(passwordEncoder.encode("user123"));
            user.setBalance(new BigDecimal("20000.00"));
            user.setAddress(userAddress);
            userRepository.save(user);
        } else {
            User user = userRepository.findByEmail(userEmail).get();
            if (!passwordEncoder.matches("user123", user.getPassword())) {
                user.setPassword(passwordEncoder.encode("user123"));
                userRepository.save(user);
            }
            if (user.getAddress() == null) {
                Address userAddress = new Address();
                userAddress.setStreet("707 Banana Avenue");
                userAddress.setCity("Lucknow");
                userAddress.setState("Uttar Pradesh");
                userAddress.setPostalCode("226001");
                userAddress.setCountry("India");
                userAddress = addressRepository.save(userAddress);
                user.setAddress(userAddress);
                userRepository.save(user);
            }
        }

        // Ensure Vendor exists
        String vendorEmail = "rohit.sona@example.com";
        if (vendorRepository.findByContactEmail(vendorEmail).isEmpty()) {
            Vendor vendor = new Vendor();
            vendor.setVendorName("Sona Jewelers");
            vendor.setDescription("Premium 24K Gold Vendor");
            vendor.setContactPersonName("Rohit Sona");
            vendor.setContactEmail(vendorEmail);
            vendor.setPassword(passwordEncoder.encode("vendor123"));
            vendor.setContactPhone("9876543211");
            vendor.setTotalGoldQuantity(new BigDecimal("1000.00"));
            vendor.setCurrentGoldPrice(new BigDecimal("6400.00"));
            vendorRepository.save(vendor);
        } else {
            Vendor vendor = vendorRepository.findByContactEmail(vendorEmail).get();
            vendor.setPassword(passwordEncoder.encode("vendor123"));
            vendorRepository.save(vendor);
        }
    }
}
