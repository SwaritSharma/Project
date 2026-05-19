package com.personal.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.project.dto.BuyPhysicalGoldRequest;
import com.personal.project.dto.ConvertToPhysicalGoldRequest;
import com.personal.project.dto.PhysicalGoldDTO;
import com.personal.project.entity.PhysicalGoldTransaction;
import com.personal.project.exception.AddressNotFoundException;
import com.personal.project.mapper.PhysicalGoldMapper;
import com.personal.project.security.jwt.JwtService;
import com.personal.project.security.service.CustomUserDetailsService;
import com.personal.project.security.service.VendorUserDetailsService;
import com.personal.project.service.PhysicalGoldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhysicalGoldController.class)
@AutoConfigureMockMvc(addFilters = false)
class PhysicalGoldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private PhysicalGoldService physicalGoldService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private VendorUserDetailsService vendorUserDetailsService;

    @MockitoBean
    private PhysicalGoldMapper physicalGoldMapper;

    @Test
    void buyPhysicalGold_validRequest_returnsPhysicalTransaction() throws Exception {
        BuyPhysicalGoldRequest request = new BuyPhysicalGoldRequest(1, 2, new BigDecimal("2.00"), 3);
        PhysicalGoldTransaction transaction = new PhysicalGoldTransaction();
        transaction.setPhysicalTransactionId(99);
        transaction.setQuantity(new BigDecimal("2.00"));
        PhysicalGoldDTO dto = new PhysicalGoldDTO();
        dto.setPhysicalTransactionId(99);
        dto.setQuantity(new BigDecimal("2.00"));

        when(physicalGoldService.buyPhysicalGold(any(BuyPhysicalGoldRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/physical-gold/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_id").value(99))
                .andExpect(jsonPath("$.quantity").value(2.00));

        verify(physicalGoldService).buyPhysicalGold(any(BuyPhysicalGoldRequest.class));
    }

    @Test
    void buyPhysicalGold_missingDeliveryAddress_returnsBadRequest() throws Exception {
        BuyPhysicalGoldRequest request = new BuyPhysicalGoldRequest(1, 2, new BigDecimal("2.00"), null);

        mockMvc.perform(post("/physical-gold/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details[0]").value("Delivery address id is required"));

        verify(physicalGoldService, never()).buyPhysicalGold(any(BuyPhysicalGoldRequest.class));
    }

    @Test
    void convertToPhysicalGold_validRequest_returnsPhysicalTransaction() throws Exception {
        ConvertToPhysicalGoldRequest request = new ConvertToPhysicalGoldRequest(1, 7, new BigDecimal("1.00"), 3);
        PhysicalGoldTransaction transaction = new PhysicalGoldTransaction();
        transaction.setPhysicalTransactionId(100);
        transaction.setQuantity(new BigDecimal("1.00"));
        PhysicalGoldDTO dto = new PhysicalGoldDTO();
        dto.setPhysicalTransactionId(100);
        dto.setQuantity(new BigDecimal("1.00"));

        when(physicalGoldService.convertToPhysicalGold(any(ConvertToPhysicalGoldRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/physical-gold/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_id").value(100))
                .andExpect(jsonPath("$.quantity").value(1.00));

        verify(physicalGoldService).convertToPhysicalGold(any(ConvertToPhysicalGoldRequest.class));
    }

    @Test
    void convertToPhysicalGold_unknownAddress_returnsNotFound() throws Exception {
        ConvertToPhysicalGoldRequest request = new ConvertToPhysicalGoldRequest(1, 7, new BigDecimal("1.00"), 404);
        when(physicalGoldService.convertToPhysicalGold(any(ConvertToPhysicalGoldRequest.class)))
                .thenThrow(new AddressNotFoundException("Delivery address not found"));

        mockMvc.perform(post("/physical-gold/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Delivery address not found"));
    }
}
