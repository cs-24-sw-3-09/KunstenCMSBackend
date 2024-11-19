package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.dto.ContentDto;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

@Component
public class ContentMapperImpl implements Mapper<ContentEntity, ContentDto> {
    private final ModelMapper modelMapper;

    public ContentMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ContentDto mapTo(ContentEntity contentEntity) {
        return modelMapper.map(contentEntity, ContentDto.class);
    }

    @Override
    public ContentEntity mapFrom(ContentDto contentDto) {
        return modelMapper.map(contentDto, ContentEntity.class);
    }

}
