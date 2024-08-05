package az.orient.entity;

import az.orient.type.Currency;
import az.orient.type.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String iban;
    private String name;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer age;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Integer userId;
}
