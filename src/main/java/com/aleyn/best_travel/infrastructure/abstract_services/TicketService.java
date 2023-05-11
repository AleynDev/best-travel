package com.aleyn.best_travel.infrastructure.abstract_services;

import com.aleyn.best_travel.api.models.request.TicketRequest;
import com.aleyn.best_travel.api.models.responses.TicketResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface TicketService extends CrudService<TicketRequest, TicketResponse, UUID> {

    BigDecimal getPriceFly(Long flyId, Currency currency);

}
