package com.project1.transaction;


import com.project1.transaction.data.AdminTransactionDTO;
import com.project1.transaction.data.Transaction;
import com.project1.transaction.data.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface TransactionMapper {
    @Mapping(target = "senderUserName", expression = "java(transaction.getSender().getFirstname() + \" \" + transaction.getSender().getLastname())")
    @Mapping(target = "receiverUserName", expression = "java(transaction.getReceiver().getFirstname() + \" \" + transaction.getReceiver().getLastname())")
    TransactionDTO toDto(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDTO);
    Transaction toEntity(AdminTransactionDTO admintransactionDTO);


}
