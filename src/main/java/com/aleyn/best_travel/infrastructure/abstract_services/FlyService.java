package com.aleyn.best_travel.infrastructure.abstract_services;

import com.aleyn.best_travel.api.models.responses.FlyResponse;

import java.util.Set;

public interface FlyService extends CatalogService<FlyResponse> {

    Set<FlyResponse> findByOriginDestiny(String origin, String destiny);

}
