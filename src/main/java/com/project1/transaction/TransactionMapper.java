package com.project1.transaction;


import com.project1.transaction.data.Transaction;
import com.project1.transaction.data.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface TransactionMapper {
    TransactionDTO toDto(Transaction transaction);
    Transaction toEntity(TransactionDTO skillDTO);
}
