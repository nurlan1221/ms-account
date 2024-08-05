package az.orient.service;

import az.orient.dto.AccountDto;
import az.orient.entity.AccountEntity;
import az.orient.handler.AccountNotFoundException;
import az.orient.mapper.AccountMapper;
import az.orient.repository.AccountRepository;
import az.orient.type.RespStatus;
import az.orient.type.Response;
import az.orient.type.Status;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    
    public AccountDto createAccount(AccountDto accountDto) {
        AccountEntity accountEntity = AccountMapper.INSTANCE.toAccountEntity(accountDto);
        accountEntity.setIban(Iban.random(CountryCode.AZ).toString());
        accountEntity.setStatus(Status.ACTIVE);
        AccountEntity saved = accountRepository.save(accountEntity);
        return AccountMapper.INSTANCE.toAccountDto(saved);
    }

    public List<AccountDto> getAllAccounts() {
        List<AccountEntity> accountEntityList = accountRepository.findAll();
        return AccountMapper.INSTANCE.toAccountDtoList(accountEntityList);
    }

    public AccountDto getAccountById(Long id) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        return AccountMapper.INSTANCE.toAccountDto(accountEntity);
    }


    public void deleteAccountById(Long id) {
        AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found:" + id));
        accountEntity.setStatus(Status.DELETED);
        accountRepository.save(accountEntity);
    }


    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found:" + id));
        AccountEntity updatedAccountEntity = AccountMapper.INSTANCE.updateAccountEntity(accountDto, accountEntity);
        AccountEntity saved = accountRepository.save(updatedAccountEntity);
        return AccountMapper.INSTANCE.toAccountDto(saved);
    }

    public AccountDto updateAccountStatus(Long id, String newStatus) {
        AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found:" + id));
        accountEntity.setStatus(Status.valueOf(newStatus));
        AccountEntity saved = accountRepository.save(accountEntity);
        return AccountMapper.INSTANCE.toAccountDto(saved);
    }
}
