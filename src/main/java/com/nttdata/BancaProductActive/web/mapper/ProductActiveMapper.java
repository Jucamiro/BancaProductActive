package com.nttdata.BancaProductActive.web.mapper;

import com.nttdata.BancaProductActive.domain.ProductActive;
import com.nttdata.BancaProductActive.web.model.ProductActiveModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductActiveMapper {
    ProductActive modelToEntity (ProductActiveModel model);

    ProductActiveModel entityToModel (ProductActive event);

    @Mapping(target="idProductActive", ignore = true)
    void update(@MappingTarget ProductActive entity, ProductActive updateEntity);
}
