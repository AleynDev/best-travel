package com.aleyn.best_travel.infrastructure.services;

import com.aleyn.best_travel.api.models.responses.HotelResponse;
import com.aleyn.best_travel.domain.entities.HotelEntity;
import com.aleyn.best_travel.domain.repositories.HotelRepository;
import com.aleyn.best_travel.infrastructure.abstract_services.HotelService;
import com.aleyn.best_travel.util.constants.CacheConstants;
import com.aleyn.best_travel.util.enums.SortType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class HotelServiceImp implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public Page<HotelResponse> readAll(Integer numPage, Integer sizePage, SortType sortType) {
        PageRequest pageRequest = null;
        switch (sortType) {
            case NONE  -> pageRequest = PageRequest.of(numPage, sizePage);
            case LOWER -> pageRequest = PageRequest.of(numPage, sizePage, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(numPage, sizePage, Sort.by(FIELD_BY_SORT).descending());
        }
        return hotelRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> readLessPrice(BigDecimal price) {
        return hotelRepository.findByPriceLessThan(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {
        return hotelRepository.findByPriceBetween(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> findRatingGreaterThan(Integer rating) {
        return hotelRepository.findByRatingGreaterThan(rating)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }


    private HotelResponse entityToResponse(HotelEntity entity) {
        var response = new HotelResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

}
