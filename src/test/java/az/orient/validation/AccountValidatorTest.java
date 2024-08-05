package az.orient.validation;

import az.orient.dto.AccountDto;
import az.orient.entity.AccountEntity;
import az.orient.repository.AccountRepository;
import az.orient.type.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Component
@ExtendWith(MockitoExtension.class)
class AccountValidatorTest {
    @Autowired
    MockMvc mockMvc;
    @Mock
    Errors errors;

    @InjectMocks
    AccountValidator accountValidator;

    @Mock
    AccountDto accountDto;

    @Mock
    Object target;

    @Autowired
    ObjectMapper objectMapper;


    @Mock
    AccountRepository accountRepository;

    @Test
    void supportsClass() throws Exception {
        assertTrue(accountValidator.supports(AccountDto.class));
    }

    @Test
    void validate() throws Exception {
        // Create an AccountDto with duplicate currency for the same user
        AccountDto accountDto = new AccountDto();
        accountDto.setUserId(1);
        accountDto.setCurrency(Currency.EUR);

        // Mock the repository to simulate an existing account with the same currency for the user
        when(accountRepository.findAllByUserIdAndCurrency(accountDto.getUserId(), accountDto.getCurrency()))
                .thenReturn(Optional.of(new AccountEntity()));

        Errors errors = new BeanPropertyBindingResult(accountDto, "accountDto");

        // Call the validate method
        accountValidator.validate(accountDto, errors);
        assertTrue(errors.hasFieldErrors("currency"));
        assertEquals("User already have account in this currency", errors.getFieldError("currency").getDefaultMessage());
    }
    @Test
    void validateGivenUniqueCurrencyThenNoErrors() {
        // Create an AccountDto with a unique currency for the user
        AccountDto accountDto = new AccountDto();
        accountDto.setUserId(1);
        accountDto.setCurrency(Currency.USD);

        // Mock the repository to return an empty result, indicating no existing account with the same currency
        when(accountRepository.findAllByUserIdAndCurrency(accountDto.getUserId(), accountDto.getCurrency()))
                .thenReturn(Optional.empty());

        Errors errors = new BeanPropertyBindingResult(accountDto, "accountDto");

        // Call the validate method
        accountValidator.validate(accountDto, errors);

        // Check if there are no errors
        assertTrue(!errors.hasFieldErrors("currency"));
    }


}