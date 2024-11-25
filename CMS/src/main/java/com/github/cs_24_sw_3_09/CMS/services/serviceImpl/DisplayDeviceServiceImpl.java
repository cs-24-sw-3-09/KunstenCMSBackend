package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DisplayDeviceServiceImpl implements DisplayDeviceService {

    private DisplayDeviceRepository displayDeviceRepository;
    private PushTSService pushTSService;

    public DisplayDeviceServiceImpl(DisplayDeviceRepository displayDeviceRepository, PushTSService pushTSService) {
        this.displayDeviceRepository = displayDeviceRepository;
        this.pushTSService = pushTSService;
    }

    @Override
    public Optional<DisplayDeviceEntity> findOne(Long id) {
        return displayDeviceRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<DisplayDeviceEntity> findAll() {
        // return itterable, so we convert it to list.
        return StreamSupport.stream(displayDeviceRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DisplayDeviceEntity> findAll(Pageable pageable) {
        return displayDeviceRepository.findAll(pageable);
    }

    @Override
    public DisplayDeviceEntity save(DisplayDeviceEntity displayDevice) {
        DisplayDeviceEntity toReturn = displayDeviceRepository.save(displayDevice);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return toReturn;
    }

    @Override
    public boolean isExists(Long id) {
        return displayDeviceRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public DisplayDeviceEntity partialUpdate(Long id, DisplayDeviceEntity displayDeviceEntity) {
        displayDeviceEntity.setId(Math.toIntExact(id));
        return displayDeviceRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            // if display device from request has name, we set it to the existing display
            // device. (same with other atts)
            Optional.ofNullable(displayDeviceEntity.getName()).ifPresent(existingDisplayDevice::setName);
            Optional.ofNullable(displayDeviceEntity.getDisplayOrientation())
                    .ifPresent(existingDisplayDevice::setDisplayOrientation);
            Optional.ofNullable(displayDeviceEntity.getConnectedState())
                    .ifPresent(existingDisplayDevice::setConnectedState);
            Optional.ofNullable(displayDeviceEntity.getLocation()).ifPresent(existingDisplayDevice::setLocation);
            Optional.ofNullable(displayDeviceEntity.getModel()).ifPresent(existingDisplayDevice::setModel);
            Optional.ofNullable(displayDeviceEntity.getResolution()).ifPresent(existingDisplayDevice::setResolution);
            Optional.ofNullable(displayDeviceEntity.getFallbackContent())
                    .ifPresent(existingDisplayDevice::setFallbackContent);

            DisplayDeviceEntity toReturn = displayDeviceRepository.save(existingDisplayDevice);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void delete(Long id) {
        displayDeviceRepository.deleteById(Math.toIntExact(id));
    }
}
