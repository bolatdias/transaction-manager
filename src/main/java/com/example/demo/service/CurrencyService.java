package com.example.demo.service;


import com.example.demo.mapper.CurrencyMapper;
import com.example.demo.model.Currency;
import com.example.demo.payload.CurrencyConversionDTO;
import com.example.demo.payload.CurrencyDTO;
import com.example.demo.payload.PagedResponse;
import com.example.demo.proxy.CurrencyProxy;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.utils.AppConst;
import com.example.demo.utils.HolidayChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CurrencyService implements ApplicationRunner {
    @Value("${app.service.apiKey}")
    private String apiKey;

    private final CurrencyProxy currencyProxy;
    private final CurrencyRepository currencyRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void parseCurrencyApiIfNotHolidayOrWeekend() {
        if (HolidayChecker.isHoliday()) {
            parseCurrencyApi(AppConst.OTHER_CURRENCY);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        parseCurrencyApi(AppConst.OTHER_CURRENCY);
    }


    @Transactional
    public void parseCurrencyApi(Set<String> parseStrings) {
        for (String parseString : parseStrings) {
            String symbol = AppConst.BASE_CURRENCY + "/" + parseString;
            CurrencyConversionDTO currencyDto = getExchangeRateApi(symbol);

            insertCurrency(currencyDto, symbol);
        }
    }

    public void insertCurrency(CurrencyConversionDTO currencyDTO, String symbol) {
        Currency currency = getCurrencyBySymbol(symbol);

        if (currency == null) {
            currency = CurrencyMapper.INSTANCE.conversionDTOToCurrency(currencyDTO);
            currency.setPreviousCloseExchange(currencyDTO.getRate());
        } else {
            currency.setPreviousCloseExchange(currency.getCloseExchange());
            currency.setCloseExchange(currencyDTO.getRate());
        }

        currencyRepository.save(currency);

    }


    private CurrencyConversionDTO getExchangeRateApi(String symbol) {
        return currencyProxy.getExchangeRate(apiKey, symbol);
    }


    public Currency getCurrencyBySymbol(String s) {

        return currencyRepository.findBySymbol(s).orElse(null);
    }


    public PagedResponse<CurrencyDTO> getAll(Pageable pageable) {
        PagedResponse<CurrencyDTO> response = new PagedResponse<>();
        Page<Currency> page = currencyRepository.findAll(pageable);

        List<CurrencyDTO> DTOlist = new ArrayList<>();
        for (Currency currency : page.getContent()) {
            DTOlist.add(CurrencyMapper.INSTANCE.conversionCurrencyToDTO(currency));
        }

        response.setPage(page.getNumber() + 1);
        response.setSize(page.getSize());
        response.setLast(page.isLast());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setContent(DTOlist);
        return response;
    }
}

