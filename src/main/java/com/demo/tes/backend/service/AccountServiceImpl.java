package com.demo.tes.backend.service;

import com.demo.tes.backend.dto.AccountDto;
import com.demo.tes.backend.dto.RequestAccountDto;
import com.demo.tes.backend.exception.security.ServiceException;
import com.demo.tes.backend.model.Account;
import com.demo.tes.backend.repostory.IAccountRepostory;
import com.demo.tes.backend.service.IAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepostory accountRepostory;
    private final ConcurrentHashMap<String, List<AccountDto>> cache = new ConcurrentHashMap<>();


    @Override
    public List<AccountDto> getData() {
        String cacheKey = "account_data";
        return cache.computeIfAbsent(cacheKey, key -> {
            String url = "https://jsonplaceholder.typicode.com/posts";
            RestTemplate restTemplate = new RestTemplate();

            Account[] accounts = restTemplate.getForObject(url, Account[].class);
            List<Account> accountList = null;
            List<AccountDto> accountDtos = new ArrayList<>();
            if (accounts != null) {
                accountList = Arrays.asList(accounts);
                accountRepostory.saveAll(accountList);
                accountDtos = accountList.stream().map(x -> {
                    AccountDto accountDto = new AccountDto();
                    accountDto.setId(x.getId());
                    accountDto.setTitle(x.getTitle());
                    return accountDto;
                }).collect(Collectors.toList());
            }
            return accountDtos;
        });
    }


    @Override
    @Cacheable(value = "accountDataPage", key = "#page + '-' + #size") // pakai redis jika ada
    public Page<AccountDto> getDataPage(Integer page, Integer size) throws ServiceException {

        Pageable pageable = PageRequest.of(
                Optional.ofNullable(page).orElse(1) - 1,
                Optional.ofNullable(size).orElse(20));

        Page<Account> accounts = accountRepostory.findAll(pageable);
        if (page != null && page > accounts.getPageable().getPageNumber()) {
            throw new ServiceException("invalid page");
        }
        List<AccountDto> accountDtos = new ArrayList<>();
        accounts.getContent().forEach(x -> {
            AccountDto accountDto = new AccountDto();
            accountDto.setId(x.getId());
            accountDto.setTitle(x.getTitle());
            accountDtos.add(accountDto);
        });

        return new PageImpl<>(accountDtos, pageable, accounts.getTotalElements());
    }

    @Override
    public AccountDto getAccountById(Long id) throws ServiceException {
        Account account = accountRepostory.findById(id).orElseThrow(() -> new ServiceException("id tidak ditemukan"));
        return AccountDto.builder().id(account.getId()).
                title(account.getTitle())
                .build();
    }

    @Override
    public Boolean saveAccount(RequestAccountDto requestAccountDto) {
        if (requestAccountDto.getTitle() == null || requestAccountDto.getTitle().isEmpty()) {
            return false;
        }
        Account account = new Account();
        account.setTitle(requestAccountDto.getTitle());
        accountRepostory.save(account);
        return true;
    }

    @Override
    public Boolean updateAccount(Long id, RequestAccountDto requestAccountDto) throws ServiceException {
        Account account = accountRepostory.findById(id).orElseThrow(() -> new ServiceException("Account not found with id: " + id));
        account.setTitle(requestAccountDto.getTitle());
        accountRepostory.save(account);
        return true;
    }

    @Override
    public Boolean deleteAccount(Long id) throws ServiceException {
        if (!accountRepostory.existsById(id)) {
            throw new ServiceException("Account not found with id: " + id);
        }
        accountRepostory.deleteById(id);
        return true;
    }
}
