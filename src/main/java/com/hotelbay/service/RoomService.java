package com.hotelbay.service;

import com.hotelbay.entity.Room;
import com.hotelbay.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public List<Room> findByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    public boolean existsById(Long id) {
        return roomRepository.existsById(id);
    }

    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }

    public void deleteAll() {
        roomRepository.deleteAll();
    }
}
