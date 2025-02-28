package com.demo.tes.backend;

import com.demo.tes.backend.dto.RequestAccountDto;
import com.demo.tes.backend.exception.security.ServiceException;
import com.demo.tes.backend.model.Account;
import com.demo.tes.backend.repostory.IAccountRepostory;
import com.demo.tes.backend.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AccountServiceImplTest {
    @Mock
    private IAccountRepostory accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testUpdateAccount_Success() throws ServiceException {
        Long accountId = 1L;
        Account existingAccount = new Account(accountId, "Title 1");
        RequestAccountDto updatedAccount = new RequestAccountDto( "Title 2");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Boolean result = accountService.updateAccount(accountId, updatedAccount);

        assertTrue(result);
        assertEquals("Title 2", existingAccount.getTitle());

        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateAccount_NotFound() throws ServiceException {
        Long accountId = 1L;
        RequestAccountDto updatedAccountDto = new RequestAccountDto("Title 2");

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Boolean result = accountService.updateAccount(accountId, updatedAccountDto);

        assertFalse(result);

        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }
}
