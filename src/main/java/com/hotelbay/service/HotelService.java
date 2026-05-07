package com.hotelbay.service;

import com.hotelbay.entity.Hotel;
import com.hotelbay.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }

    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    public List<Hotel> findByActive(boolean active) {
        return hotelRepository.findByActive(active);
    }

    public boolean existsById(Long id) {
        return hotelRepository.existsById(id);
    }

    public void deleteById(Long id) {
        hotelRepository.deleteById(id);
    }

    public void deleteAll() {
        hotelRepository.deleteAll();
    }
}
