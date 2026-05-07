package com.hotelbay.repository;

import com.hotelbay.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    List<Room> findByAvailable(boolean available);
    List<Room> findByRoomType(String roomType);
    List<Room> findByHotelIdAndAvailable(Long hotelId, boolean available);
}
