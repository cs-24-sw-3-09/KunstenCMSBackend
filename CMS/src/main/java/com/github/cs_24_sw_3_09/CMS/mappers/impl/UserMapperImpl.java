package com.github.cs_24_sw_3_09.CMS.mappers.impl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

    private ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(UserEntity UserEntity) {
        return modelMapper.map(UserEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto UserDto) {
        return modelMapper.map(UserDto, UserEntity.class);
    }
}
