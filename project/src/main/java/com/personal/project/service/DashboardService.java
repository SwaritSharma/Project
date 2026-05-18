package com.personal.project.service;

import com.personal.project.dto.DashboardDTO;
import com.personal.project.dto.EditProfileRequest;
import com.personal.project.dto.HoldingDTO;
import com.personal.project.dto.PaymentDTO;
import com.personal.project.dto.PhysicalGoldDTO;
import com.personal.project.dto.TransactionDTO;

import java.util.List;

public interface DashboardService {

    DashboardDTO getDashboard(Integer userId);

    void updateProfile(Integer userId, EditProfileRequest request);

    List<HoldingDTO> getHoldings(Integer userId);

    List<TransactionDTO> getTransactions(Integer userId);

    List<HoldingDTO.AddressDTO> getAddresses(Integer userId);

    List<PhysicalGoldDTO> getPhysicalGold(Integer userId);

    List<PaymentDTO> getPayments(Integer userId);
}
