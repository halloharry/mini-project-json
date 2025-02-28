package com.demo.tes.backend.endpoint;

import com.demo.tes.backend.dto.AccountDto;
import com.demo.tes.backend.dto.RequestAccountDto;
import com.demo.tes.backend.exception.security.ServiceException;
import com.demo.tes.backend.model.Account;
import com.demo.tes.backend.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    private final IAccountService accountService;

    @GetMapping("/fetch-data")
    public List<AccountDto> fetchData(
    ) {
        return accountService.getData();
    }

    @GetMapping("/get-data")
    public Page<AccountDto> getDataPage(
            @Param("page") Integer page,
            @Param("page") Integer size
    ) throws ServiceException {
        return accountService.getDataPage(page, size);
    }

    @GetMapping("/{id}")
    public AccountDto getAccountById(@PathVariable Long id) throws ServiceException {
        return accountService.getAccountById(id);
    }

    @PostMapping("/add")
    public Boolean createAccount(@RequestBody RequestAccountDto requestAccountDto) {
        return accountService.saveAccount(requestAccountDto);
    }

    @PutMapping("/{id}")
    public Boolean updateAccount(@PathVariable Long id, @RequestBody RequestAccountDto requestAccountDto) throws ServiceException {
        return accountService.updateAccount(id, requestAccountDto);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteAccount(@PathVariable Long id) throws ServiceException {
        return accountService.deleteAccount(id);
    }
}
