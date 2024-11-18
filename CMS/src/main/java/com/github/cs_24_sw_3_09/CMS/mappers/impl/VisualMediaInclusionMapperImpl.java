package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaInclusionDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import org.modelmapper.ModelMapper;

public class VisualMediaInclusionMapperImpl implements Mapper<VisualMediaInclusionEntity, VisualMediaInclusionDto> {

    private final ModelMapper modelMapper;

    public VisualMediaInclusionMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public VisualMediaInclusionDto mapTo(VisualMediaInclusionEntity visualMediaInclusionEntity) {
        return modelMapper.map(visualMediaInclusionEntity, VisualMediaInclusionDto.class);
    }

    @Override
    public VisualMediaInclusionEntity mapFrom(VisualMediaInclusionDto visualMediaInclusionDto) {
        return modelMapper.map(visualMediaInclusionDto, VisualMediaInclusionEntity.class);
    }
}
