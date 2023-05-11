package com.aleyn.best_travel.infrastructure.abstract_services;

import com.aleyn.best_travel.api.models.request.ReservationRequest;
import com.aleyn.best_travel.api.models.responses.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface ReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID> {

    BigDecimal getPriceHotel(Long id, Currency currency);

}
