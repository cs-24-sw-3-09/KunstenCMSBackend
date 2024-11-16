package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VisualMediaMapperImpl implements Mapper<VisualMediaEntity, VisualMediaDto> {

    private ModelMapper modelMapper;

    public VisualMediaMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public VisualMediaDto mapTo(VisualMediaEntity visualMediaEntity) {
        return modelMapper.map(visualMediaEntity, VisualMediaDto.class);
    }

    @Override
    public VisualMediaEntity mapFrom(VisualMediaDto visualMediaDto) {
        return modelMapper.map(visualMediaDto, VisualMediaEntity.class);
    }
}
