package com.hotelbay.repository;

import com.hotelbay.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuestId(Long guestId);
    List<Reservation> findByHotelId(Long hotelId);
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    List<Reservation> findByGuestIdAndStatus(Long guestId, Reservation.ReservationStatus status);
    List<Reservation> findByHotelIdAndStatus(Long hotelId, Reservation.ReservationStatus status);
    
    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND " +
           "((r.checkInDate <= :checkIn AND r.checkOutDate > :checkIn) OR " +
           "(r.checkInDate < :checkOut AND r.checkOutDate >= :checkOut) OR " +
           "(r.checkInDate >= :checkIn AND r.checkOutDate <= :checkOut))")
    List<Reservation> findConflictingReservations(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);
}
