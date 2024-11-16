package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VisualMediaServiceImpl implements VisualMediaService {


    private VisualMediaRepository visualMediaRepository;

    public VisualMediaServiceImpl(VisualMediaRepository visualMediaRepository) {
        this.visualMediaRepository = visualMediaRepository;
    }

    @Override
    public VisualMediaEntity createVisualMedia(VisualMediaEntity visualMedia) {
        return visualMediaRepository.save(visualMedia);
    }

    @Override
    public List<VisualMediaEntity> findAll() {
        return StreamSupport.stream(visualMediaRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<VisualMediaEntity> findOne(Long id) {
        return visualMediaRepository.findById(Math.toIntExact(id));
    }
}
