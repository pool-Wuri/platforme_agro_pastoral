package com.anptic.agropastoral.mappers;

import com.anptic.agropastoral.dto.stockmovement.StockMovementRequest;
import com.anptic.agropastoral.dto.stockmovement.StockMovementResponse;
import com.anptic.agropastoral.model.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {StockMapper.class, UserMapper.class})
public interface StockMovementMapper {

    StockMovementMapper INSTANCE = Mappers.getMapper(StockMovementMapper.class);

    StockMovementResponse toStockMovementResponse(StockMovement stockMovement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "previousQuantity", ignore = true)
    @Mapping(target = "newQuantity", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "user", ignore = true)
    StockMovement toStockMovement(StockMovementRequest stockMovementRequest);
}
