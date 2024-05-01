package com.example.demo.mapper;

import com.example.demo.model.Transaction;
import com.example.demo.payload.TransactionRequestDTO;
import com.example.demo.payload.TransactionResponseDTO;
import com.example.demo.utils.AppConst;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mappings({
            @Mapping(source = "accountFrom", target = "accountFrom"),
            @Mapping(source = "accountTo", target = "accountTo"),
            @Mapping(source = "sum", target = "sum"),
            @Mapping(source = "type", target = "expenseCategory"),
            @Mapping(source = "datetime", target = "datetime")
    })
    Transaction convertDTOtoModel(TransactionRequestDTO requestDTO);

    @Mappings({
            @Mapping(source = "accountFrom", target = "accountFrom"),
            @Mapping(source = "accountTo", target = "accountTo"),
            @Mapping(target = "currencyShortname", expression = "java(transaction.getCurrency().getSymbol())"),
            @Mapping(source = "sum", target = "sum"),
            @Mapping(source = "expenseCategory", target = "expenseCategory"),
            @Mapping(source = "datetime", target = "datetime"),
            @Mapping(target = "limitSum", source = "limit.limitValue"),
            @Mapping(target = "limitDatetime", source = "datetime"),
            @Mapping(target = "limitCurrencyShortname", constant = AppConst.BASE_CURRENCY)
    })
    TransactionResponseDTO convertModelToDTO(Transaction transaction);
}
