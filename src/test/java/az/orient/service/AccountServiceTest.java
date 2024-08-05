package az.orient.service;

import az.orient.dto.AccountDto;
import az.orient.entity.AccountEntity;
import az.orient.handler.AccountNotFoundException;
import az.orient.repository.AccountRepository;
import az.orient.type.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;


    @Test
    void updateAccountGivenValidIdThenReturnUpdatedAccountDto() {
        //setup

        Long accountId = 1L;
        AccountDto request = new AccountDto();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName("Mock");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        when(accountRepository.save(accountEntity)).thenReturn(accountEntity);
        //when
        AccountDto response = accountService.updateAccount(accountId, request);

        //then
        assertNotNull(response);
        Mockito.verify(accountRepository, Mockito.times(1)).save(accountEntity);

    }

    @Test
    void updateAccountGivenInvalidIdThenThrowAccountNotFoundException() {
        //setup
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        AccountNotFoundException result = assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(accountId, new AccountDto()));
        assertNotNull(result);

    }

    @Test
    void deleteAccountGivenValidIdThenReturnTrue() {
        Long accountId = 1L;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountId);
        accountEntity.setStatus(Status.ACTIVE);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        accountService.deleteAccountById(accountId);
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(accountEntity);
        assertEquals(Status.DELETED, accountEntity.getStatus());
    }

    @Test
    void deleteAccountGivenInvalidIdThenThrowAccountNotFoundException() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        AccountNotFoundException result = assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccountById(accountId));
        assertNotNull(result);
    }

    @Test
    void getAccountGivenValidIdThenReturnAccountDto() {
        Long accountId = 1L;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountId);
        accountEntity.setName("Mock");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        AccountDto response = accountService.getAccountById(accountId);
        assertNotNull(response);
    }

    @Test
    void getAccountGivenInvalidIdThenThrowAccountNotFoundException() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        AccountNotFoundException result = assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(accountId));
        assertNotNull(result);
    }


}