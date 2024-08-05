package az.orient.mapper;

import az.orient.dto.AccountDto;
import az.orient.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountEntity toAccountEntity(AccountDto accountDto);

    AccountDto toAccountDto(AccountEntity accountEntity);

    List<AccountDto> toAccountDtoList(List<AccountEntity> accountEntities);
    @Mapping(target = "iban",ignore = true)
    @Mapping(target="status",ignore = true)
    @Mapping(target = "id",ignore = true)
    AccountEntity updateAccountEntity(AccountDto accountDto,@MappingTarget AccountEntity accountEntity);
}
