package com.personal.project.repository;

import com.personal.project.entity.Address;
import com.personal.project.entity.Payment;
import com.personal.project.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Address createAddress(
            String city,
            String postalCode
    ) {

        Address address = new Address();

        address.setStreet("Sector 17");

        address.setCity(city);

        address.setState("Punjab");

        address.setPostalCode(postalCode);

        address.setCountry("India");

        return addressRepository.save(address);
    }

    private User createUser(
            String name,
            String email,
            BigDecimal balance,
            Address address
    ) {

        User user = new User();

        user.setName(name);

        user.setEmail(email);

        user.setPassword("password123");

        user.setBalance(balance);

        user.setCreatedAt(
                LocalDateTime.now()
        );

        user.setAddress(address);

        return userRepository.save(user);
    }

    private Payment createPayment(
            User user,
            BigDecimal amount,
            String paymentMethod,
            String transactionType,
            String paymentStatus,
            LocalDateTime createdAt
    ) {

        Payment payment = new Payment();

        payment.setUser(user);

        payment.setAmount(amount);

        payment.setPaymentMethod(
                paymentMethod
        );

        payment.setTransactionType(
                transactionType
        );

        payment.setPaymentStatus(
                paymentStatus
        );

        payment.setCreatedAt(createdAt);

        return paymentRepository.save(payment);
    }

    @Test
    @DisplayName(
            "Should Find Payments By User Id"
    )
    void shouldFindPaymentsByUserId() {

        Address address =
                createAddress(
                        "Delhi",
                        "110001"
                );

        User user =
                createUser(
                        "Friday",
                        "friday@gmail.com",
                        BigDecimal.valueOf(100000),
                        address
                );

        createPayment(
                user,
                BigDecimal.valueOf(5000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.now()
        );

        createPayment(
                user,
                BigDecimal.valueOf(10000),
                "PhonePe",
                "Debited from wallet",
                "Success",
                LocalDateTime.now()
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserId(
                                user.getUserId(),
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
        ).hasSize(2);
    }

    @Test
    @DisplayName(
            "Should Filter Payments By Payment Method"
    )
    void shouldFilterPaymentsByPaymentMethod() {

        Address address =
                createAddress(
                        "Mumbai",
                        "400001"
                );

        User user =
                createUser(
                        "Aman",
                        "aman@gmail.com",
                        BigDecimal.valueOf(70000),
                        address
                );

        createPayment(
                user,
                BigDecimal.valueOf(10000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.now()
        );

        createPayment(
                user,
                BigDecimal.valueOf(20000),
                "PhonePe",
                "Debited from wallet",
                "Success",
                LocalDateTime.now()
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserIdAndPaymentMethod(
                                user.getUserId(),
                                "Google Pay",
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
        ).hasSize(1);

        assertThat(
                payments.getContent()
                        .get(0)
                        .getPaymentMethod()
        ).isEqualTo("Google Pay");
    }

    @Test
    @DisplayName(
            "Should Filter Payments By Transaction Type"
    )
    void shouldFilterPaymentsByTransactionType() {

        Address address =
                createAddress(
                        "Delhi",
                        "110001"
                );

        User user =
                createUser(
                        "Rohit",
                        "rohit@gmail.com",
                        BigDecimal.valueOf(60000),
                        address
                );

        createPayment(
                user,
                BigDecimal.valueOf(5000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.now()
        );

        createPayment(
                user,
                BigDecimal.valueOf(2000),
                "PhonePe",
                "Debited from wallet",
                "Success",
                LocalDateTime.now()
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserIdAndTransactionType(
                                user.getUserId(),
                                "Credited to wallet",
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
        ).hasSize(1);

        assertThat(
                payments.getContent()
                        .get(0)
                        .getTransactionType()
        ).isEqualTo("Credited to wallet");
    }

    @Test
    @DisplayName(
            "Should Filter Payments By Payment Status"
    )
    void shouldFilterPaymentsByPaymentStatus() {

        Address address =
                createAddress(
                        "Pune",
                        "411001"
                );

        User user =
                createUser(
                        "Test",
                        "test@gmail.com",
                        BigDecimal.valueOf(50000),
                        address
                );

        createPayment(
                user,
                BigDecimal.valueOf(5000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.now()
        );

        createPayment(
                user,
                BigDecimal.valueOf(7000),
                "PhonePe",
                "Debited from wallet",
                "Failed",
                LocalDateTime.now()
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserIdAndPaymentStatus(
                                user.getUserId(),
                                "Failed",
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
        ).hasSize(1);

        assertThat(
                payments.getContent()
                        .get(0)
                        .getPaymentStatus()
        ).isEqualTo("Failed");
    }

    @Test
    @DisplayName(
            "Should Sort Payments By Created At Desc"
    )
    void shouldSortPaymentsByCreatedAtDesc() {

        Address address =
                createAddress(
                        "Delhi",
                        "110001"
                );

        User user =
                createUser(
                        "Friday",
                        "friday@gmail.com",
                        BigDecimal.valueOf(90000),
                        address
                );

        createPayment(
                user,
                BigDecimal.valueOf(1000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.of(
                        2025,
                        1,
                        1,
                        10,
                        0
                )
        );

        createPayment(
                user,
                BigDecimal.valueOf(2000),
                "PhonePe",
                "Debited from wallet",
                "Success",
                LocalDateTime.of(
                        2025,
                        1,
                        2,
                        10,
                        0
                )
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserIdOrderByCreatedAtDesc(
                                user.getUserId(),
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
                        .get(0)
                        .getAmount()
        ).isEqualTo(
                BigDecimal.valueOf(2000)
        );
    }

    @Test
    @DisplayName(
            "Should Sort Payments By Created At Asc"
    )
    void shouldSortPaymentsByCreatedAtAsc() {

        Address address =
                createAddress(
                        "Mumbai",
                        "400001"
                );

        User user =
                createUser(
                        "Aman",
                        "aman@gmail.com",
                        BigDecimal.valueOf(80000),
                        address
                );

        createPayment(
                user,
                BigDecimal.valueOf(3000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.of(
                        2025,
                        1,
                        2,
                        10,
                        0
                )
        );

        createPayment(
                user,
                BigDecimal.valueOf(1000),
                "PhonePe",
                "Debited from wallet",
                "Success",
                LocalDateTime.of(
                        2025,
                        1,
                        1,
                        10,
                        0
                )
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserIdOrderByCreatedAtAsc(
                                user.getUserId(),
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
                        .get(0)
                        .getAmount()
        ).isEqualTo(
                BigDecimal.valueOf(1000)
        );
    }

    @Test
    @DisplayName(
            "Should Sort Payments By Amount Desc"
    )
    void shouldSortPaymentsByAmountDesc() {

        Address address =
                createAddress(
                        "Delhi",
                        "110001"
                );

        User user =
                createUser(
                        "Rohit",
                        "rohit@gmail.com",
                        BigDecimal.valueOf(100000),
                        address
                );

        createPayment(
                user,
                BigDecimal.valueOf(5000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.now()
        );

        createPayment(
                user,
                BigDecimal.valueOf(15000),
                "PhonePe",
                "Debited from wallet",
                "Success",
                LocalDateTime.now()
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserIdOrderByAmountDesc(
                                user.getUserId(),
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
                        .get(0)
                        .getAmount()
        ).isEqualTo(
                BigDecimal.valueOf(15000)
        );
    }

    @Test
    @DisplayName(
            "Should Sort Payments By Amount Asc"
    )
    void shouldSortPaymentsByAmountAsc() {

        Address address =
                createAddress(
                        "Chandigarh",
                        "160017"
                );

        User user =
                createUser(
                        "Test",
                        "test@gmail.com",
                        BigDecimal.valueOf(50000),
                        address
                );

        createPayment(
                user,
                BigDecimal.valueOf(20000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.now()
        );

        createPayment(
                user,
                BigDecimal.valueOf(5000),
                "PhonePe",
                "Debited from wallet",
                "Success",
                LocalDateTime.now()
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserIdOrderByAmountAsc(
                                user.getUserId(),
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
                        .get(0)
                        .getAmount()
        ).isEqualTo(
                BigDecimal.valueOf(5000)
        );
    }

    @Test
    @DisplayName(
            "Should Return User Specific Payments Only"
    )
    void shouldReturnUserSpecificPaymentsOnly() {

        Address address1 =
                createAddress(
                        "Delhi",
                        "110001"
                );

        Address address2 =
                createAddress(
                        "Mumbai",
                        "400001"
                );

        User user1 =
                createUser(
                        "User1",
                        "user1@gmail.com",
                        BigDecimal.valueOf(50000),
                        address1
                );

        User user2 =
                createUser(
                        "User2",
                        "user2@gmail.com",
                        BigDecimal.valueOf(70000),
                        address2
                );

        createPayment(
                user1,
                BigDecimal.valueOf(5000),
                "Google Pay",
                "Credited to wallet",
                "Success",
                LocalDateTime.now()
        );

        createPayment(
                user2,
                BigDecimal.valueOf(15000),
                "PhonePe",
                "Debited from wallet",
                "Success",
                LocalDateTime.now()
        );

        Page<Payment> payments =
                paymentRepository
                        .findByUserUserId(
                                user1.getUserId(),
                                PageRequest.of(0, 10)
                        );

        assertThat(
                payments.getContent()
        ).hasSize(1);

        assertThat(
                payments.getContent()
                        .get(0)
                        .getUser()
                        .getName()
        ).isEqualTo("User1");
    }
}