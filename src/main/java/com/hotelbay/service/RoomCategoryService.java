package com.hotelbay.service;

import com.hotelbay.entity.RoomCategory;
import com.hotelbay.repository.RoomCategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomCategoryService {
    private final RoomCategoryRepository roomCategoryRepository;

    public RoomCategoryService(RoomCategoryRepository roomCategoryRepository) {
        this.roomCategoryRepository = roomCategoryRepository;
    }

    public List<RoomCategory> findAll() {
        return roomCategoryRepository.findAll();
    }

    public Optional<RoomCategory> findById(Long id) {
        return roomCategoryRepository.findById(id);
    }

    public List<RoomCategory> findByName(String name) {
        return roomCategoryRepository.findAll().stream()
                .filter(category -> category.getName() != null && category.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<RoomCategory> findByParentCategory(Long parentCategoryId) {
        return roomCategoryRepository.findByParentCategoryId(parentCategoryId);
    }

    public List<RoomCategory> findRootCategories() {
        return roomCategoryRepository.findByParentCategoryIdIsNull();
    }

    public RoomCategory save(RoomCategory category) {
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        if (category.getUpdatedAt() == null) {
            category.setUpdatedAt(LocalDateTime.now());
        }
        return roomCategoryRepository.save(category);
    }

    public void deleteById(Long id) {
        roomCategoryRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return roomCategoryRepository.existsById(id);
    }
}
