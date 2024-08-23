package com.github.druzhininyury.testtask.mapping;

import com.github.druzhininyury.testtask.dto.OperationDto;
import com.github.druzhininyury.testtask.model.Operation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    Operation toOperation(OperationDto operationDto);

}
