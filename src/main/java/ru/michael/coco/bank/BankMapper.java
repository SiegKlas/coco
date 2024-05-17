package ru.michael.coco.bank;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankMapper {
    BankDTO toDTO(Bank bank);

    Bank toEntity(BankDTO bankDTO);
}
