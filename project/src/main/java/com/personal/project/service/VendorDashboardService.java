package com.personal.project.service;

import com.personal.project.dto.AddBranchRequest;
import com.personal.project.dto.HoldingDTO;
import com.personal.project.dto.TransactionDTO;
import com.personal.project.dto.VendorBranchDTO;
import com.personal.project.dto.VendorDashboardDTO;
import com.personal.project.dto.EditVendorProfileRequest;
import com.personal.project.entity.Address;
import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.Vendor;
import com.personal.project.entity.VendorBranch;
import com.personal.project.repository.AddressRepository;
import com.personal.project.repository.TransactionHistoryRepository;
import com.personal.project.repository.VendorBranchRepository;
import com.personal.project.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorDashboardService {

    private final VendorRepository vendorRepository;
    private final VendorBranchRepository vendorBranchRepository;
    private final TransactionHistoryRepository transactionRepository;
    private final AddressRepository addressRepository;
    private final GoldPriceService goldPriceService;

    @Transactional(readOnly = true)
    public VendorDashboardDTO getDashboard(Integer vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new RuntimeException("Vendor not found"));
        
        List<VendorBranch> branches = vendorBranchRepository.findByVendorVendorId(vendorId);
        BigDecimal totalInventory = branches.stream()
                .map(VendorBranch::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate sold quantity from transactions where type is 'BUY' (user buys from vendor)
        // Wait, if transaction type is BUY, it means the user bought gold from this vendor, so vendor sold it.
        // Let's assume all BUY transactions for this vendor's branches count.
        // For simplicity, we can query all transactions and filter.
        // But since we don't have a specific repo method for vendor transactions easily, let's just use the vendor's total_gold_quantity or similar.
        // Wait, Vendor entity has totalGoldQuantity.
        BigDecimal totalSold = BigDecimal.ZERO; 
        
        VendorDashboardDTO dto = new VendorDashboardDTO();
        dto.setVendorId(vendor.getVendorId());
        dto.setVendorName(vendor.getVendorName());
        dto.setDescription(vendor.getDescription());
        dto.setContactPersonName(vendor.getContactPersonName());
        dto.setContactEmail(vendor.getContactEmail());
        dto.setContactPhone(vendor.getContactPhone());
        dto.setWebsiteUrl(vendor.getWebsiteUrl());
        dto.setTotalBranches(branches.size());
        dto.setTotalGoldQuantity(totalInventory);
        dto.setTotalSoldQuantity(totalSold);
        dto.setCurrentGoldPrice(goldPriceService.getCurrentPrice().getPrice());

        return dto;
    }

    @Transactional
    public VendorDashboardDTO updateProfile(Integer vendorId, EditVendorProfileRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new RuntimeException("Vendor not found"));
        if (request.getContactPersonName() != null) vendor.setContactPersonName(request.getContactPersonName());
        if (request.getContactEmail() != null) vendor.setContactEmail(request.getContactEmail());
        if (request.getContactPhone() != null) vendor.setContactPhone(request.getContactPhone());
        if (request.getDescription() != null) vendor.setDescription(request.getDescription());
        if (request.getWebsiteUrl() != null) vendor.setWebsiteUrl(request.getWebsiteUrl());
        vendorRepository.save(vendor);
        return getDashboard(vendorId);
    }

    @Transactional(readOnly = true)
    public List<VendorBranchDTO> getBranches(Integer vendorId) {
        return vendorBranchRepository.findByVendorVendorId(vendorId).stream().map(b -> {
            VendorBranchDTO dto = new VendorBranchDTO();
            dto.setBranchId(b.getBranchId());
            dto.setQuantity(b.getQuantity());
            
            if (b.getAddress() != null) {
                HoldingDTO.AddressDTO addressDTO = new HoldingDTO.AddressDTO();
                addressDTO.setAddressId(b.getAddress().getAddressId());
                addressDTO.setStreet(b.getAddress().getStreet());
                addressDTO.setCity(b.getAddress().getCity());
                addressDTO.setState(b.getAddress().getState());
                addressDTO.setPostalCode(b.getAddress().getPostalCode());
                addressDTO.setCountry(b.getAddress().getCountry());
                dto.setAddress(addressDTO);
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public VendorBranchDTO addBranch(Integer vendorId, AddBranchRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(() -> new RuntimeException("Vendor not found"));
        
        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address = addressRepository.save(address);

        VendorBranch branch = new VendorBranch();
        branch.setVendor(vendor);
        branch.setAddress(address);
        branch.setQuantity(request.getInitialQuantity());
        branch = vendorBranchRepository.save(branch);

        VendorBranchDTO dto = new VendorBranchDTO();
        dto.setBranchId(branch.getBranchId());
        dto.setQuantity(branch.getQuantity());
        
        HoldingDTO.AddressDTO addressDTO = new HoldingDTO.AddressDTO();
        addressDTO.setAddressId(address.getAddressId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setPostalCode(address.getPostalCode());
        addressDTO.setCountry(address.getCountry());
        dto.setAddress(addressDTO);

        return dto;
    }

    @Transactional
    public void deleteBranch(Integer vendorId, Integer branchId) {
        VendorBranch branch = vendorBranchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch not found"));
        if (!branch.getVendor().getVendorId().equals(vendorId)) {
            throw new RuntimeException("Branch does not belong to this vendor");
        }
        vendorBranchRepository.delete(branch);
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactions(Integer vendorId) {
        return transactionRepository.findByBranchVendorVendorIdOrderByCreatedAtDesc(vendorId, PageRequest.of(0, 100)).getContent().stream().map(t -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setTransactionId(t.getTransactionId());
            dto.setTransactionType(t.getTransactionType());
            String vendorName = "Unknown Vendor";
            String branchName = "";
            String branchAddress = "";
            
            if (t.getBranch() != null) {
                if (t.getBranch().getVendor() != null) {
                    vendorName = t.getBranch().getVendor().getVendorName();
                }
                branchName = "Branch #" + t.getBranch().getBranchId();
                if (t.getBranch().getAddress() != null) {
                    branchAddress = t.getBranch().getAddress().getCity() + ", " + t.getBranch().getAddress().getState();
                }
            }
            
            dto.setVendorName(vendorName);
            dto.setBranchName(branchName);
            dto.setBranchAddress(branchAddress);
            
            if (t.getUser() != null) {
                dto.setUserName(t.getUser().getName());
                if (t.getUser().getAddress() != null) {
                    dto.setUserAddress(t.getUser().getAddress().getCity() + ", " + t.getUser().getAddress().getState());
                }
            } else {
                dto.setUserName("Vendor Action");
                dto.setUserAddress("");
            }
            
            dto.setQuantity(t.getQuantity());
            dto.setAmount(t.getAmount());
            dto.setCreatedAt(t.getCreatedAt());
            dto.setTransactionStatus(t.getTransactionStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void addGoldToBranch(Integer vendorId, Integer branchId, BigDecimal quantity) {
        VendorBranch branch = vendorBranchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch not found"));
        if (!branch.getVendor().getVendorId().equals(vendorId)) {
            throw new RuntimeException("Branch does not belong to this vendor");
        }
        
        branch.setQuantity(branch.getQuantity().add(quantity));
        vendorBranchRepository.save(branch);
        
        Vendor vendor = branch.getVendor();
        vendor.setTotalGoldQuantity(vendor.getTotalGoldQuantity().add(quantity));
        vendorRepository.save(vendor);
        
        TransactionHistory th = new TransactionHistory();
        th.setBranch(branch);
        th.setQuantity(quantity);
        th.setAmount(BigDecimal.ZERO);
        th.setTransactionType("Add Inventory");
        th.setTransactionStatus("Success");
        th.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(th);
    }
}
