package com.example.devhouse.tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Long> {
        Tag findByNameIgnoreCase(String name);

}
