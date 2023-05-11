package com.aleyn.best_travel.infrastructure.abstract_services;

import com.aleyn.best_travel.api.models.responses.HotelResponse;

import java.util.Set;

public interface HotelService extends CatalogService<HotelResponse> {

    Set<HotelResponse> findRatingGreaterThan(Integer rating);

}
