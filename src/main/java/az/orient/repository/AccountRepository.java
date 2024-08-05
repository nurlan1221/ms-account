package az.orient.repository;

import az.orient.entity.AccountEntity;
import az.orient.type.Currency;
import az.orient.type.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    AccountEntity findByIdAndStatus(Long id, Status status);

    Optional<AccountEntity> findAllByUserIdAndCurrency(Integer userId, Currency currency);

    Optional<AccountEntity> findById(Long id);
}
