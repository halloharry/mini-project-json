package com.demo.tes.backend;

import com.demo.tes.backend.dto.AccountDto;
import com.demo.tes.backend.exception.security.ServiceException;
import com.demo.tes.backend.model.Account;
import com.demo.tes.backend.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
