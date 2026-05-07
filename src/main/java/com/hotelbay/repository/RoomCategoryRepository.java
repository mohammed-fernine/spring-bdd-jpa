package com.hotelbay.repository;

import com.hotelbay.entity.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Long> {
    List<RoomCategory> findByParentCategoryId(Long parentCategoryId);
    List<RoomCategory> findByParentCategoryIdIsNull();
}
