package com.aleyn.best_travel.infrastructure.services;

import com.aleyn.best_travel.api.models.request.TicketRequest;
import com.aleyn.best_travel.api.models.responses.FlyResponse;
import com.aleyn.best_travel.api.models.responses.TicketResponse;
import com.aleyn.best_travel.domain.entities.jpa.TicketEntity;
import com.aleyn.best_travel.domain.repositories.jpa.CustomerRepository;
import com.aleyn.best_travel.domain.repositories.jpa.FlyRepository;
import com.aleyn.best_travel.domain.repositories.jpa.TicketRepository;
import com.aleyn.best_travel.infrastructure.abstract_services.TicketService;
import com.aleyn.best_travel.infrastructure.helpers.ApiCurrencyConnectorHelper;
import com.aleyn.best_travel.infrastructure.helpers.BlackListHelper;
import com.aleyn.best_travel.infrastructure.helpers.CustomerHelper;
import com.aleyn.best_travel.infrastructure.helpers.EmailHelper;
import com.aleyn.best_travel.util.BestTravelUtil;
import com.aleyn.best_travel.util.enums.Tables;
import com.aleyn.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TicketServiceImp implements TicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper currencyConnectorHelper;
    private final EmailHelper emailHelper;

    @Override
    public TicketResponse create(TicketRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var fly = flyRepository.findById(request.getIdFly())
                .orElseThrow(() -> new IdNotFoundException(Tables.FLY.name()));

        var customer = customerRepository.findById(request.getIdClient())
                .orElseThrow(() -> new IdNotFoundException(Tables.CUSTOMER.name()));;

        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(CHARGE_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();

        var ticketPersisted = ticketRepository.save(ticketToPersist);

        customerHelper.incrase(customer.getDni(), TicketServiceImp.class);

        if (Objects.nonNull(request.getEmail())) {
            this.emailHelper.sendMail(
                    request.getEmail(),
                    customer.getFullName(),
                    Tables.TICKET.name().toLowerCase()
            );
        }

        log.info("Ticket saved with id: {}", ticketPersisted.getId());

        return entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID id) {
        var ticketFromDB = ticketRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(Tables.TICKET.name()));

        return entityToResponse(ticketFromDB);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID id) {
        var ticketToUpdate = ticketRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(Tables.TICKET.name()));

        var fly = flyRepository.findById(request.getIdFly())
                .orElseThrow(() -> new IdNotFoundException(Tables.FLY.name()));

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(CHARGE_PRICE_PERCENTAGE)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLater());

        var ticketUpdated = ticketRepository.save(ticketToUpdate);

        log.info("Ticket updated with id {}", ticketUpdated.getId());

        return entityToResponse(ticketUpdated);
    }

    @Override
    public void delete(UUID id) {
        var ticketToDelete = ticketRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(Tables.TICKET.name()));

        ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal getPriceFly(Long flyId, Currency currency) {
        var fly = flyRepository.findById(flyId)
                .orElseThrow(() -> new IdNotFoundException(Tables.FLY.name()));

        var priceInDollars = fly.getPrice().add(fly.getPrice().multiply(CHARGE_PRICE_PERCENTAGE));

        if (currency.equals(Currency.getInstance("USD"))) return priceInDollars;

        var currencyDTO = this.currencyConnectorHelper.getCurrency(currency);
        log.info("API Currency in {}, response: {}", currencyDTO.getExchangeDate(), currencyDTO.getRates());

        return priceInDollars.multiply(currencyDTO.getRates().get(currency));

    }

    private TicketResponse entityToResponse(TicketEntity entity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }

    public static final BigDecimal CHARGE_PRICE_PERCENTAGE = BigDecimal.valueOf(0.25);

}
