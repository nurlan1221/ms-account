package az.orient.dto;

import az.orient.type.Currency;
import az.orient.type.Status;
import az.orient.validation.ValidAccountName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class AccountDto {
    private Long id;
    private String iban;
    @ValidAccountName(isNullable = true)
    private String name;
    @NotNull(message = "Currency is required")
    private Currency currency;
    @NotNull(message = "Balance is required")
    private BigDecimal balance;
    private Status status;
    private Integer userId;

}
