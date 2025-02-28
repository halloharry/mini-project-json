package com.demo.tes.backend.service;

import com.demo.tes.backend.dto.AccountDto;
import com.demo.tes.backend.exception.security.ServiceException;
import com.demo.tes.backend.model.Account;
import com.demo.tes.backend.repostory.IAccountRepostory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService{

    private final IAccountRepostory accountRepostory;

    @Override
    public List<AccountDto> getData() {
        String url = "https://jsonplaceholder.typicode.com/posts";
        RestTemplate restTemplate = new RestTemplate();

        Account[] accounts = restTemplate.getForObject(url, Account[].class);
        List<Account> accountList = null;
        List<AccountDto> accountDtos = new ArrayList<>();
        if (accounts != null) {
             accountList = Arrays.asList(accounts);
             accountRepostory.saveAll(accountList);
            accountList.stream().peek(x -> {
                AccountDto accountDto = new AccountDto();
                accountDto.setId(x.getId());
                accountDto.setTitle(x.getTitle());
                accountDtos.add(accountDto);
            }).collect(Collectors.toList());
        }
        return accountDtos;
    }


    @Override
    public Page<AccountDto> getDataPage(Integer page, Integer size) throws ServiceException {

        Pageable pageable = PageRequest.of(
                Optional.ofNullable(page).orElse(1) - 1,
                Optional.ofNullable(size).orElse(20));

        Page<Account> accounts = accountRepostory.findAll(pageable);
        if (page != null && page > accounts.getPageable().getPageNumber()) {
            throw new ServiceException("invalid page");
        }
        List<AccountDto> accountDtos = new ArrayList<>();
        accounts.getContent().forEach(x-> {
            AccountDto accountDto = new AccountDto();
            accountDto.setId(x.getId());
            accountDto.setTitle(x.getTitle());
            accountDtos.add(accountDto);
        });

        return new PageImpl<>(accountDtos, pageable, accounts.getTotalElements());
    }
}
