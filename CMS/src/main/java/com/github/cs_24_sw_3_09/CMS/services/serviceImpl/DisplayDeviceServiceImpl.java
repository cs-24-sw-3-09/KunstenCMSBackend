package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DisplayDeviceServiceImpl implements DisplayDeviceService {

    private DisplayDeviceRepository displayDeviceRepository;

    @Override
    public Optional<DisplayDeviceEntity> findOne(Long id) {
        return displayDeviceRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<DisplayDeviceEntity> findAll() {
        //return itterable, so we convert it to list.
        return StreamSupport.stream(displayDeviceRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public DisplayDeviceServiceImpl(DisplayDeviceRepository displayDeviceRepository) {
        this.displayDeviceRepository = displayDeviceRepository;
    }

    @Override
    public DisplayDeviceEntity createDisplayDevice(DisplayDeviceEntity displayDevice) {
        // Er basically bare en pass through method i dette tilfælde.
        return displayDeviceRepository.save(displayDevice);
    }
}
