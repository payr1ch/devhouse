package com.example.devhouse.tag;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private TagRepo tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Optional<Tag> getTagById(Long tagId) {
        return tagRepository.findById(tagId);
    }

    public Tag getTagByName(String name) {
        return tagRepository.findByNameIgnoreCase(name);
    }
}
