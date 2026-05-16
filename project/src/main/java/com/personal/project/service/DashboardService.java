package com.personal.project.service;

import com.personal.project.dto.DashboardDTO;
import com.personal.project.dto.HoldingDTO;
import com.personal.project.dto.PhysicalGoldDTO;
import com.personal.project.dto.TransactionDTO;
import com.personal.project.dto.EditProfileRequest;
import com.personal.project.entity.User;
import com.personal.project.entity.Address;
import com.personal.project.entity.VirtualGoldHolding;
import com.personal.project.repository.TransactionHistoryRepository;
import com.personal.project.repository.UserRepository;
import com.personal.project.repository.AddressRepository;
import com.personal.project.repository.VirtualGoldHoldingRepository;
import com.personal.project.repository.PhysicalGoldTransactionRepository;
import com.personal.project.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final VirtualGoldHoldingRepository holdingRepository;
    private final TransactionHistoryRepository transactionRepository;
    private final PhysicalGoldTransactionRepository physicalGoldTransactionRepository;
    private final PaymentRepository paymentRepository;
    private final AddressRepository addressRepository;
    private final GoldPriceService goldPriceService;

    @Transactional(readOnly = true)
    public DashboardDTO getDashboard(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        
        List<VirtualGoldHolding> holdings = holdingRepository.findByUserUserId(userId, PageRequest.of(0, 1000)).getContent();
        
        BigDecimal totalGrams = holdings.stream()
                .map(VirtualGoldHolding::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal currentPrice = goldPriceService.getCurrentPrice().getPrice();
        BigDecimal totalValue = totalGrams.multiply(currentPrice).setScale(2, RoundingMode.HALF_UP);
        
        // Mock PnL since we don't store average buy price
        BigDecimal pnlAmount = totalValue.multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal pnlPercent = new BigDecimal("15.00");
        
        DashboardDTO dto = new DashboardDTO();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBalance(user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO);
        dto.setTotalHoldingsGrams(totalGrams);
        dto.setTotalHoldingsValue(totalValue);
        dto.setCurrentGoldPrice(currentPrice);
        dto.setPnlAmount(pnlAmount);
        dto.setPnlPercent(pnlPercent);
        
        return dto;
    }

    @Transactional
    public void updateProfile(Integer userId, EditProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (request.getStreet() != null && !request.getStreet().isEmpty()) {
            Address address = user.getAddress();
            if (address == null) {
                address = new Address();
            }
            address.setStreet(request.getStreet());
            address.setCity(request.getCity());
            address.setState(request.getState());
            address.setPostalCode(request.getPostalCode());
            address.setCountry(request.getCountry());
            address = addressRepository.save(address);
            user.setAddress(address);
        }

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<HoldingDTO> getHoldings(Integer userId) {
        BigDecimal currentPrice = goldPriceService.getCurrentPrice().getPrice();
        return holdingRepository.findByUserUserId(userId, PageRequest.of(0, 1000)).getContent().stream().map(h -> {
            HoldingDTO dto = new HoldingDTO();
            dto.setHoldingId(h.getHoldingId());
            String vendorName = "Unknown Vendor";
            HoldingDTO.AddressDTO addressDTO = new HoldingDTO.AddressDTO();
            if (h.getBranch() != null) {
                if (h.getBranch().getVendor() != null) {
                    vendorName = h.getBranch().getVendor().getVendorName();
                }
                if (h.getBranch().getAddress() != null) {
                    addressDTO.setStreet(h.getBranch().getAddress().getStreet());
                    addressDTO.setCity(h.getBranch().getAddress().getCity());
                    addressDTO.setState(h.getBranch().getAddress().getState());
                    addressDTO.setPostalCode(h.getBranch().getAddress().getPostalCode());
                }
            }
            dto.setVendorName(vendorName);
            dto.setBranchAddress(addressDTO);
            dto.setQuantity(h.getQuantity());
            
            BigDecimal val = h.getQuantity().multiply(currentPrice).setScale(2, RoundingMode.HALF_UP);
            dto.setCurrentValue(val);
            dto.setValue(val);
            dto.setCurrentGoldPrice(currentPrice);
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactions(Integer userId) {
        return transactionRepository.findByUserUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 100)).getContent().stream().map(t -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setTransactionId(t.getTransactionId());
            dto.setTransactionType(t.getTransactionType());
            String vendorName = "Unknown Vendor";
            if (t.getBranch() != null && t.getBranch().getVendor() != null) {
                vendorName = t.getBranch().getVendor().getVendorName();
            }
            dto.setVendorName(vendorName);
            dto.setQuantity(t.getQuantity());
            dto.setAmount(t.getAmount());
            dto.setCreatedAt(t.getCreatedAt());
            dto.setTransactionStatus(t.getTransactionStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HoldingDTO.AddressDTO> getAddresses(Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            if (user.getAddress() != null) {
                HoldingDTO.AddressDTO addressDTO = new HoldingDTO.AddressDTO();
                addressDTO.setAddressId(user.getAddress().getAddressId());
                addressDTO.setStreet(user.getAddress().getStreet());
                addressDTO.setCity(user.getAddress().getCity());
                addressDTO.setState(user.getAddress().getState());
                addressDTO.setPostalCode(user.getAddress().getPostalCode());
                addressDTO.setCountry(user.getAddress().getCountry());
                return java.util.Collections.singletonList(addressDTO);
            }
            return new java.util.ArrayList<>();
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return new java.util.ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in getAddresses: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<PhysicalGoldDTO> getPhysicalGold(Integer userId) {
        return physicalGoldTransactionRepository.findByUserUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 100)).getContent().stream().map(t -> {
            PhysicalGoldDTO dto = new PhysicalGoldDTO();
            dto.setPhysicalTransactionId(t.getPhysicalTransactionId());
            dto.setQuantity(t.getQuantity());
            dto.setCreatedAt(t.getCreatedAt());

            String vendorName = "Unknown Vendor";
            if (t.getBranch() != null && t.getBranch().getVendor() != null) {
                vendorName = t.getBranch().getVendor().getVendorName();
            }
            dto.setVendorName(vendorName);

            if (t.getDeliveryAddress() != null) {
                HoldingDTO.AddressDTO addressDTO = new HoldingDTO.AddressDTO();
                addressDTO.setStreet(t.getDeliveryAddress().getStreet());
                addressDTO.setCity(t.getDeliveryAddress().getCity());
                addressDTO.setState(t.getDeliveryAddress().getState());
                addressDTO.setPostalCode(t.getDeliveryAddress().getPostalCode());
                dto.setDeliveryAddress(addressDTO);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<com.personal.project.dto.PaymentDTO> getPayments(Integer userId) {
        return paymentRepository.findByUserUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 100)).getContent().stream().map(p -> {
            com.personal.project.dto.PaymentDTO dto = new com.personal.project.dto.PaymentDTO();
            dto.setPaymentId(p.getPaymentId());
            dto.setTransactionType(p.getTransactionType());
            dto.setPaymentMethod(p.getPaymentMethod());
            dto.setAmount(p.getAmount());
            dto.setPaymentStatus(p.getPaymentStatus());
            dto.setCreatedAt(p.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}
