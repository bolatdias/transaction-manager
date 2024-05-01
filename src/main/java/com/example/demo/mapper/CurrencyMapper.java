package com.example.demo.mapper;

import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import com.example.demo.payload.CurrencyDTO;
import com.example.demo.utils.AppConst;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(source = "symbol", target = "symbol")
    @Mapping(source = "rate", target = "closeExchange")
    Currency conversionDTOToCurrency(CurrencyConversionDTO currencyDTO);

    @Mapping(target = "baseSymbol", expression = "java(com.example.demo.utils.AppConst.BASE_CURRENCY)")
    CurrencyDTO conversionCurrencyToDTO(Currency currency);

}
