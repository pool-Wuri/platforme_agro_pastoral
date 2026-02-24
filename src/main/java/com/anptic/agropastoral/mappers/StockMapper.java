package com.anptic.agropastoral.mappers;

import com.anptic.agropastoral.dto.stock.StockRequest;
import com.anptic.agropastoral.dto.stock.StockResponse;
import com.anptic.agropastoral.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    StockResponse toStockResponse(Stock stock);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Stock toStock(StockRequest stockRequest);
}
