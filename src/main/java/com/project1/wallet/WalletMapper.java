package com.project1.wallet;

import com.project1.wallet.data.Wallet;
import com.project1.wallet.data.WalletDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletDTO walletToWalletDTO(Wallet wallet);
    Wallet walletDTOToWallet(WalletDTO walletDTO);
}