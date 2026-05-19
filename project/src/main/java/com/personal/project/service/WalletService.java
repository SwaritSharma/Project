package com.personal.project.service;

import com.personal.project.dto.WalletTopupRequest;
import com.personal.project.dto.UserDTO;

public interface WalletService {

    UserDTO topupWallet(
            WalletTopupRequest request
    );
}