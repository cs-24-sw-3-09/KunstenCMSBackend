package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SlideshowMapperImpl implements Mapper<SlideshowEntity, SlideshowDto> {
    private ModelMapper modelMapper;

    public SlideshowMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SlideshowDto mapTo(SlideshowEntity slideshowEntity) {
        return modelMapper.map(slideshowEntity, SlideshowDto.class);
    }

    @Override
    public SlideshowEntity mapFrom(SlideshowDto SlideshowDto) {
        return modelMapper.map(SlideshowDto, SlideshowEntity.class);
    }

}
