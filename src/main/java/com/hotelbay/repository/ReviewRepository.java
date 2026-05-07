package com.hotelbay.repository;

import com.hotelbay.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHotelId(Long hotelId);
    List<Review> findByGuestId(Long guestId);
    List<Review> findByReservationId(Long reservationId);
    List<Review> findByHotelIdAndGuestId(Long hotelId, Long guestId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.hotel.id = :hotelId")
    Double findAverageRatingByHotel(@Param("hotelId") Long hotelId);
    
    Long countByHotelId(Long hotelId);
    List<Review> findByHotelIdOrderByRatingDesc(Long hotelId);
    List<Review> findByRatingBetween(Integer minRating, Integer maxRating);
}
