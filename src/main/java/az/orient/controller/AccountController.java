package az.orient.controller;

import az.orient.dto.AccountDto;
import az.orient.service.AccountService;
import az.orient.type.Response;
import az.orient.validation.AccountValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountValidator accountValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getTarget() != null && accountValidator.supports(binder.getTarget().getClass())) {
            binder.addValidators(accountValidator);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody @Valid AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @GetMapping
    public List<AccountDto> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(path = "{id}")
    public AccountDto getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @DeleteMapping(path = "{id}")
    public void deleteAccountById(@PathVariable Long id) {
        accountService.deleteAccountById(id);
    }

    @PutMapping(path = "{id}")
    public AccountDto updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        return accountService.updateAccount(id, accountDto);
    }

    @PatchMapping(path = "{id}/status")
    public AccountDto updateAccountStatus(@PathVariable Long id, String status) {
        return accountService.updateAccountStatus(id, status);
    }
}
