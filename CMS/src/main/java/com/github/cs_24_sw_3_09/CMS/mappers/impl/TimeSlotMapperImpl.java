package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TimeSlotMapperImpl implements Mapper<TimeSlotEntity, TimeSlotDto> {
    
    private ModelMapper modelMapper;

    public TimeSlotMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TimeSlotDto mapTo(TimeSlotEntity timeSlotEntity) {
        return modelMapper.map(timeSlotEntity, TimeSlotDto.class);
    }

    @Override
    public TimeSlotEntity mapFrom(TimeSlotDto timeSlotDto) {
        return modelMapper.map(timeSlotDto, TimeSlotEntity.class);
    }
}
