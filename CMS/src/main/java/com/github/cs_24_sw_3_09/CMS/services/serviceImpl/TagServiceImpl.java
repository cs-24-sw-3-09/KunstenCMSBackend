package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.TagRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final VisualMediaRepository visualMediaRepository;

    public TagServiceImpl(TagRepository tagRepository, VisualMediaRepository visualMediaRepository) {
        this.tagRepository = tagRepository;
        this.visualMediaRepository = visualMediaRepository;
    }

    @Override
    public TagEntity save(TagEntity tag) {
        return tagRepository.save(tag);
    }

    @Override
    public boolean isExists(Long id) {
        return tagRepository.existsById(id);
    }

    @Override
    public Optional<TagEntity> findOne(Long tagId) {
        return tagRepository.findById(tagId);
    }

    @Override
    public Page<TagEntity> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with id " + id + " not found"));

        // Remove the TagEntity from each associated VisualMediaEntity
        for (VisualMediaEntity media : tag.getVisualMedias()) {
            media.getTags().remove(tag); // Update from the owning side
        }

        visualMediaRepository.saveAll(tag.getVisualMedias());
        tagRepository.delete(tag);
    }

    @Override
    public TagEntity partialUpdate(Long id, TagEntity tagEntity) {
        tagEntity.setId(Math.toIntExact(id));
        return tagRepository.findById(id).map(existingTagEntity -> {
            Optional.ofNullable(tagEntity.getText()).ifPresent(existingTagEntity::setText);
            return tagRepository.save(existingTagEntity);
        }).orElseThrow(() -> new RuntimeException("Tag does not exist"));
    }
}
