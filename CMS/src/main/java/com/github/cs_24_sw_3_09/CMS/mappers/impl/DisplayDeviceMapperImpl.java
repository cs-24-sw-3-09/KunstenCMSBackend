package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DisplayDeviceMapperImpl implements Mapper<DisplayDeviceEntity, DisplayDeviceDto> {

    private ModelMapper modelMapper;

    public DisplayDeviceMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DisplayDeviceDto mapTo(DisplayDeviceEntity displayDeviceEntity) {
        return modelMapper.map(displayDeviceEntity, DisplayDeviceDto.class);
    }

    @Override
    public DisplayDeviceEntity mapFrom(DisplayDeviceDto displayDeviceDto) {
        return modelMapper.map(displayDeviceDto, DisplayDeviceEntity.class);
    }
}
