package com.hotelbay.service;

import com.hotelbay.entity.Review;
import com.hotelbay.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findByHotelId(Long hotelId) {
        return reviewRepository.findByHotelId(hotelId);
    }

    public List<Review> findByGuestId(Long guestId) {
        return reviewRepository.findByGuestId(guestId);
    }

    public List<Review> findByReservationId(Long reservationId) {
        return reviewRepository.findByReservationId(reservationId);
    }

    public List<Review> findByHotelAndGuest(Long hotelId, Long guestId) {
        return reviewRepository.findByHotelIdAndGuestId(hotelId, guestId);
    }

    public Double findAverageRatingByHotel(Long hotelId) {
        Double average = reviewRepository.findAverageRatingByHotel(hotelId);
        return average != null ? average : 0.0;
    }

    public Long countReviewsByHotel(Long hotelId) {
        return reviewRepository.countByHotelId(hotelId);
    }

    public List<Review> findByHotelOrderByRatingDesc(Long hotelId) {
        return reviewRepository.findByHotelIdOrderByRatingDesc(hotelId);
    }

    public List<Review> findByRatingRange(Integer minRating, Integer maxRating) {
        return reviewRepository.findByRatingBetween(minRating, maxRating);
    }

    public boolean existsById(Long id) {
        return reviewRepository.existsById(id);
    }

    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    public void deleteAll() {
        reviewRepository.deleteAll();
    }
}
