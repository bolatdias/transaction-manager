package com.example.demo.mapper;

import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(source = "symbol", target = "symbol")
    @Mapping(target = "exchangeDate", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(source = "rate", target = "closeExchange")
    Currency conversionDTOToCurrency(CurrencyConversionDTO currencyDTO);
}
