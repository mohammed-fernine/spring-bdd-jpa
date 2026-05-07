package com.hotelbay.service;

import com.hotelbay.entity.Reservation;
import com.hotelbay.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByGuestId(Long guestId) {
        return reservationRepository.findByGuestId(guestId);
    }

    public List<Reservation> findByHotelId(Long hotelId) {
        return reservationRepository.findByHotelId(hotelId);
    }

    public List<Reservation> findByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    public List<Reservation> findByStatus(Reservation.ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    public List<Reservation> findByGuestAndStatus(Long guestId, Reservation.ReservationStatus status) {
        return reservationRepository.findByGuestIdAndStatus(guestId, status);
    }

    public List<Reservation> findByHotelAndStatus(Long hotelId, Reservation.ReservationStatus status) {
        return reservationRepository.findByHotelIdAndStatus(hotelId, status);
    }

    public List<Reservation> findConflictingReservations(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> reservations = reservationRepository.findConflictingReservations(roomId, checkIn, checkOut);
        return reservations.stream()
                .filter(r -> r.getStatus() != Reservation.ReservationStatus.CANCELED)
                .toList();
    }

    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public void deleteAll() {
        reservationRepository.deleteAll();
    }
}
