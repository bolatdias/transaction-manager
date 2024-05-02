package com.example.demo.mapper;


import com.example.demo.model.Limit;
import com.example.demo.payload.LimitSetRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LimitMapper {
    LimitMapper INSTANCE = Mappers.getMapper( LimitMapper.class );

    @Mapping(source = "limit", target = "limitValue")
    Limit limitSetRequestToLimit(LimitSetRequestDTO request);
}
