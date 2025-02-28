package com.demo.tes.backend.service;

import com.demo.tes.backend.dto.AccountDto;
import com.demo.tes.backend.dto.RequestAccountDto;
import com.demo.tes.backend.exception.security.ServiceException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAccountService {

    List<AccountDto> getData();
    Page<AccountDto> getDataPage(Integer page, Integer size) throws ServiceException;
    AccountDto getAccountById(Long id) throws ServiceException;
    Boolean saveAccount(RequestAccountDto requestAccountDto);
    Boolean updateAccount(Long id, RequestAccountDto requestAccountDto) throws ServiceException;
    Boolean deleteAccount(Long id) throws ServiceException;
}
