package az.orient.validation;

import az.orient.dto.AccountDto;
import az.orient.entity.AccountEntity;
import az.orient.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountValidator implements Validator {
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountDto accountDto = (AccountDto) target;
        Optional<AccountEntity> accountDtoInDb = accountRepository.findAllByUserIdAndCurrency(accountDto.getUserId(), accountDto.getCurrency());
        if (accountDtoInDb.isPresent()) {
            errors.rejectValue("currency","" ,"User already have account in this currency");
        }
    }
}
