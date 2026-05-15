package com.personal.project.service;

import com.personal.project.dto.WalletTopupRequest;
import com.personal.project.entity.User;

public interface WalletService {

    User topupWallet(
            WalletTopupRequest request
    );
}