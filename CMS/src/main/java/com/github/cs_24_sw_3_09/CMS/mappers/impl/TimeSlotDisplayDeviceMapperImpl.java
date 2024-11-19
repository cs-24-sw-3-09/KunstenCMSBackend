package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import org.modelmapper.ModelMapper;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotDisplayDeviceEntity;


public class TimeSlotDisplayDeviceMapperImpl implements Mapper<TimeSlotDisplayDeviceEntity, TimeSlotDisplayDeviceDto> {
    
    private ModelMapper modelMapper;

    public TimeSlotDisplayDeviceMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TimeSlotDisplayDeviceDto mapTo(TimeSlotDisplayDeviceEntity timeSlotDisplayDeviceEntity) {
        return modelMapper.map(timeSlotDisplayDeviceEntity, TimeSlotDisplayDeviceDto.class);
    }

    @Override
    public TimeSlotDisplayDeviceEntity mapFrom(TimeSlotDisplayDeviceDto timeSlotDisplayDeviceDto) {
        return modelMapper.map(timeSlotDisplayDeviceDto, TimeSlotDisplayDeviceEntity.class);
    }
}
