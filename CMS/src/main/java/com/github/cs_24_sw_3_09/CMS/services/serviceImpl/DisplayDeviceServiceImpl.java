package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public DisplayDeviceEntity save(DisplayDeviceEntity displayDevice) {
        return displayDeviceRepository.save(displayDevice);
    }

    @Override
    public boolean isExists(Long id) {
        return displayDeviceRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public DisplayDeviceEntity partialUpdate(Long id, DisplayDeviceEntity displayDeviceEntity) {
        displayDeviceEntity.setId(Math.toIntExact(id));
        return displayDeviceRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            // if display device from request has name, we set it to the existing display device. (same with other atts)
            Optional.ofNullable(displayDeviceEntity.getName()).ifPresent(existingDisplayDevice::setName);
            Optional.ofNullable(displayDeviceEntity.getDisplayOrientation()).ifPresent(existingDisplayDevice::setDisplayOrientation);
            Optional.ofNullable(displayDeviceEntity.getConnectedState()).ifPresent(existingDisplayDevice::setConnectedState);
            Optional.ofNullable(displayDeviceEntity.getLocation()).ifPresent(existingDisplayDevice::setLocation);
            Optional.ofNullable(displayDeviceEntity.getModel()).ifPresent(existingDisplayDevice::setModel);
            Optional.ofNullable(displayDeviceEntity.getResolution()).ifPresent(existingDisplayDevice::setResolution);

            return displayDeviceRepository.save(existingDisplayDevice);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }
}
