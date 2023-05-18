package com.example.devhouse.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepo extends JpaRepository<Tag, Long> {
        Tag findByNameIgnoreCase(String name);

    List<Tag> findByNameContainingIgnoreCase(String tag);
}
