package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.BankDto;

import java.util.List;

public interface IBankService {
    Boolean synchronizeBanks();

    List<BankDto> getBanks();
}
