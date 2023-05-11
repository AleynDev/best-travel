package com.aleyn.best_travel.domain.repositories;

import com.aleyn.best_travel.domain.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    Set<HotelEntity> findByPriceLessThan(BigDecimal price);

    Set<HotelEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    Set<HotelEntity> findByRatingGreaterThan(Integer rating);

    Optional<HotelEntity> findByReservationsId(UUID id);

}
