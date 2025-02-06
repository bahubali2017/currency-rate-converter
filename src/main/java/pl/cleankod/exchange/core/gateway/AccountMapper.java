package pl.cleankod.exchange.core.gateway;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.cleankod.exchange.core.domain.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "number", target = "number.value")
    @Mapping(source = "amount", target = "balance.amount")
    @Mapping(source = "currency", target = "balance.currency")
    Account toAccount(AccountDto accountDto);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "number.value", target = "number")
    @Mapping(source = "balance.amount", target = "amount")
    @Mapping(source = "balance.currency", target = "currency")
    pl.cleankod.exchange.core.gateway.AccountDto toAccountDto(Account account);
}
