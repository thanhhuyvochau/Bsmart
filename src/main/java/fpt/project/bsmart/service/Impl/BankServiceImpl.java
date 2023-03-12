package fpt.project.bsmart.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import fpt.project.bsmart.entity.Bank;
import fpt.project.bsmart.entity.BankDto;
import fpt.project.bsmart.repository.BankRepository;
import fpt.project.bsmart.service.IBankService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.ObjectUtil;
import fpt.project.bsmart.util.rest.RestCaller;
import fpt.project.bsmart.util.rest.response.BankResponse;
import fpt.project.bsmart.util.rest.response.BankWrapperResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements IBankService {
    private final RestCaller caller;
    private final BankRepository bankRepository;
    @Value("${bank.url}")
    private String bankUrl;

    public BankServiceImpl(RestCaller caller, BankRepository bankRepository) {
        this.caller = caller;
        this.bankRepository = bankRepository;
    }

    @Override
    public Boolean synchronizeBanks() {
        TypeReference<BankWrapperResponse> typeReference = new TypeReference<BankWrapperResponse>() {
        };
        try {
            BankWrapperResponse bankWrapperResponse = caller.get(bankUrl, "", typeReference);
            List<Bank> newBanks = new ArrayList<>();
            for (BankResponse bankResponse : bankWrapperResponse.getData()) {
                Optional<Bank> persisBank = bankRepository.findByCode(bankResponse.getCode());
                if (!persisBank.isPresent()) {
                    Bank newBank = ObjectUtil.copyProperties(bankResponse, new Bank(), Bank.class);
                    newBanks.add(newBank);
                }
            }
            bankRepository.saveAll(newBanks);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<BankDto> getBanks() {
        List<Bank> banks = bankRepository.findAll();
        return banks.stream().map(ConvertUtil::convertBankToBankDto).collect(Collectors.toList());
    }
}
