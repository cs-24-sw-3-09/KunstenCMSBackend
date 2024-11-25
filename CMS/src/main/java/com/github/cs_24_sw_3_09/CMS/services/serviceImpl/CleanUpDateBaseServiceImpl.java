package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotRepository;
import com.github.cs_24_sw_3_09.CMS.services.CleanUpDateBaseService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CleanUpDateBaseServiceImpl implements CleanUpDateBaseService {

    private TimeSlotRepository timeSlotRepository;

    public CleanUpDateBaseServiceImpl(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public int deleteTSWithoutDD() {
        List<TimeSlotEntity> listTS = timeSlotRepository.findTimeSlotsWithNoDisplayDevices();
        int i = 0;
        for (TimeSlotEntity timeSlotEntity : listTS) {
            timeSlotRepository.deleteById(timeSlotEntity.getId());
            i++;
        }
        return i;
    }
}
