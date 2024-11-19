package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TagMapperImpl implements Mapper<TagEntity, TagDto> {

    private ModelMapper modelMapper;

    public TagMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto mapTo(TagEntity tagEntity) {
        return modelMapper.map(tagEntity, TagDto.class);
    }

    @Override
    public TagEntity mapFrom(TagDto tagDto) {
        return modelMapper.map(tagDto, TagEntity.class);
    }
}
