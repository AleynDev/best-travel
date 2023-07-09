package com.aleyn.best_travel.infrastructure.services;

import com.aleyn.best_travel.api.models.request.ReservationRequest;
import com.aleyn.best_travel.api.models.responses.HotelResponse;
import com.aleyn.best_travel.api.models.responses.ReservationResponse;
import com.aleyn.best_travel.domain.entities.jpa.ReservationEntity;
import com.aleyn.best_travel.domain.repositories.jpa.CustomerRepository;
import com.aleyn.best_travel.domain.repositories.jpa.HotelRepository;
import com.aleyn.best_travel.domain.repositories.jpa.ReservationRepository;
import com.aleyn.best_travel.infrastructure.abstract_services.ReservationService;
import com.aleyn.best_travel.infrastructure.helpers.ApiCurrencyConnectorHelper;
import com.aleyn.best_travel.infrastructure.helpers.BlackListHelper;
import com.aleyn.best_travel.infrastructure.helpers.CustomerHelper;
import com.aleyn.best_travel.infrastructure.helpers.EmailHelper;
import com.aleyn.best_travel.util.enums.Tables;
import com.aleyn.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ReservationServiceImp implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final HotelRepository hotelRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper currencyConnectorHelper;
    private final EmailHelper emailHelper;


    // TODO remove test data
    @Override
    public ReservationResponse create(ReservationRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var hotel = hotelRepository.findById(request.getIdHotel())
                .orElseThrow(() -> new IdNotFoundException(Tables.HOTEL.name()));

        var customer = customerRepository.findById(request.getIdClient())
                .orElseThrow(() -> new IdNotFoundException(Tables.CUSTOMER.name()));

        var reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now()) //
                .dateStart(LocalDate.now()) //
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays())) //
                .totalDays(request.getTotalDays()) //
                .price(hotel.getPrice().add(hotel.getPrice().multiply(CHARGE_PRICE_PERCENTAGE)))
                .build();

        var reservationPersisted = reservationRepository.save(reservationToPersist);

        customerHelper.incrase(customer.getDni(), ReservationServiceImp.class);

        if (Objects.nonNull(request.getEmail())) {
            this.emailHelper.sendMail(
                    request.getEmail(),
                    customer.getFullName(),
                    Tables.RESERVATION.name().toLowerCase()
            );
        }

        log.info("Reservation saved with id: {}", reservationPersisted.getId());

        return entityToResponse(reservationPersisted);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        var reservation = reservationRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.RESERVATION.name()));

        return entityToResponse(reservation);
    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID uuid) {
        var hotel = hotelRepository.findById(request.getIdHotel())
                .orElseThrow(() -> new IdNotFoundException(Tables.HOTEL.name()));

        var reservationToUpdate = reservationRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.RESERVATION.name()));

        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(CHARGE_PRICE_PERCENTAGE)));

        var reservationUpdated = reservationRepository.save(reservationToUpdate);

        log.info("Reservation update with id: {}", reservationUpdated.getId());

        return entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID uuid) {
        var reservationToDelete = reservationRepository.findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.RESERVATION.name()));

        reservationRepository.delete(reservationToDelete);
    }

    @Override
    public BigDecimal getPriceHotel(Long id, Currency currency) {
        var hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(Tables.HOTEL.name()));

        var priceInDollars = hotel.getPrice().add(hotel.getPrice().multiply(CHARGE_PRICE_PERCENTAGE));

        if (currency.equals(Currency.getInstance("USD"))) return priceInDollars;

        var currencyDTO = this.currencyConnectorHelper.getCurrency(currency);
        log.info("API Currency in {}, response: {}", currencyDTO.getExchangeDate(), currencyDTO.getRates());

        return priceInDollars.multiply(currencyDTO.getRates().get(currency));

    }


    public static final BigDecimal CHARGE_PRICE_PERCENTAGE = BigDecimal.valueOf(0.20);


    private ReservationResponse entityToResponse(ReservationEntity entity) {
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }
}
