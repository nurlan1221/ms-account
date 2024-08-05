package az.orient.controller;

import az.orient.dto.AccountDto;
import az.orient.handler.AccountNotFoundException;
import az.orient.service.AccountService;
import az.orient.type.Currency;
import az.orient.type.Status;
import az.orient.validation.AccountValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = AccountController.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @MockBean
    AccountValidator accountValidator;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createAccountGivenValidDtoThenReturn201() throws Exception {
        //setup
        AccountDto accountDto = new AccountDto();
        accountDto.setName("Nurlan");
        accountDto.setBalance(BigDecimal.ONE);
        accountDto.setCurrency(Currency.AZN);

        // when
        doNothing().when(accountValidator).validate(Mockito.any(), Mockito.any());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                .content(objectMapper.writeValueAsString(accountDto))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        //expect
        assertEquals(201, mvcResult.getResponse().getStatus());

    }

    @Test
    void updateAccountGivenNotValidIdThenReturn404() throws Exception {
        //setup
        Long accountId = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setName("Nurlan");
        accountDto.setBalance(BigDecimal.ONE);
        accountDto.setCurrency(Currency.AZN);


        // when
        AccountNotFoundException accountNotFoundException = new AccountNotFoundException("Account not found exception" + accountId);
        Mockito.when(accountService.updateAccount(accountId, accountDto)).thenThrow(accountNotFoundException);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/accounts/" + accountId)
                .content(objectMapper.writeValueAsString(accountDto))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        //expect
        assertEquals(404, mvcResult.getResponse().getStatus());

    }

    @Test
    void updateAccountGivenValidDtoThenReturn200() throws Exception {
        //setup
        Long accountId = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setName("Nurlan");
        accountDto.setBalance(BigDecimal.ONE);
        accountDto.setCurrency(Currency.AZN);


        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/accounts/" + accountId)
                .content(objectMapper.writeValueAsString(accountDto))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteAccountGivenValidIdThenReturn200() throws Exception {
        Long accountId = 1L;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/" + accountId)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        verify(accountService).deleteAccountById(accountId);
    }

    @Test
    void deleteAccountGivenInvalidIdThenReturn404() throws Exception {
        Long accountId = 1L;
        doThrow(new AccountNotFoundException("Account not found:1")).when(accountService).deleteAccountById(accountId);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/" + accountId)).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        verify(accountService).deleteAccountById(accountId);
    }

    @Test
    void getAccountGivenValidIdThenReturn200() throws Exception {
        Long accountId = 1L;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + accountId)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        verify(accountService).getAccountById(accountId);
    }

    @Test
    void getAccountGivenInvalidIdThenReturn404() throws Exception {
        Long accountId = 1L;
        when(accountService.getAccountById(accountId))
                .thenThrow(new AccountNotFoundException("Account not found: " + accountId));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + accountId)).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        verify(accountService).getAccountById(accountId);
    }

    @Test
    void updateAccountStatusGivenValidIdAndStatusThenReturn200() throws Exception {
        Long accountId = 1L;
        String newStatus = "ACTIVE";
        AccountDto request = new AccountDto();
        request.setStatus(Status.valueOf(newStatus));

        when(accountService.updateAccountStatus(accountId, newStatus)).thenReturn(request);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/" + accountId + "/status")
                        .param("status", newStatus))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        verify(accountService).updateAccountStatus(accountId, newStatus);

    }

    @Test
    void updateAccountGivenNotValidIdAndStatusThenReturn404() throws Exception {
        Long accountId = 1L;
        String newStatus = "ACTIVE";
        when(accountService.updateAccountStatus(accountId, newStatus))
                .thenThrow(new AccountNotFoundException("Account not found: " + accountId));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/" + accountId + "/status")
                        .param("status", newStatus))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        verify(accountService).updateAccountStatus(accountId, newStatus);

    }

    @Test
    void handleMethodArgumentNotValidExceptionThenReturn400() throws Exception {
        // Creating an invalid AccountDto with missing required fields
        AccountDto invalidAccountDto = new AccountDto();
        // Currency and balance are not set to trigger validation errors

        String accountDtoJson = objectMapper.writeValueAsString(invalidAccountDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountDtoJson))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());

        String responseContent = mvcResult.getResponse().getContentAsString();

        // Add assertions here to validate the exact error messages in the response
        assertEquals("application/json", mvcResult.getResponse().getContentType());

        // Use a JSON library or simple string contains check to validate the error messages
        assertTrue(responseContent.contains("Currency is required"));
        assertTrue(responseContent.contains("Balance is required"));

    }


}