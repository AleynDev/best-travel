package com.aleyn.best_travel.infrastructure.services;

import com.aleyn.best_travel.api.models.request.TourRequest;
import com.aleyn.best_travel.api.models.responses.TourResponse;
import com.aleyn.best_travel.domain.entities.jpa.*;
import com.aleyn.best_travel.domain.repositories.jpa.CustomerRepository;
import com.aleyn.best_travel.domain.repositories.jpa.FlyRepository;
import com.aleyn.best_travel.domain.repositories.jpa.HotelRepository;
import com.aleyn.best_travel.domain.repositories.jpa.TourRepository;
import com.aleyn.best_travel.infrastructure.abstract_services.TourService;
import com.aleyn.best_travel.infrastructure.helpers.BlackListHelper;
import com.aleyn.best_travel.infrastructure.helpers.CustomerHelper;
import com.aleyn.best_travel.infrastructure.helpers.EmailHelper;
import com.aleyn.best_travel.infrastructure.helpers.TourHelper;
import com.aleyn.best_travel.util.enums.Tables;
import com.aleyn.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TourServiceImp implements TourService {

    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final TourHelper tourHelper;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;

    @Override
    public TourResponse create(TourRequest request) {
        blackListHelper.isInBlackListCustomer(request.getCustomerId());
        var customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IdNotFoundException(Tables.CUSTOMER.name()));

        var flights = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> flights.add(flyRepository.findById(fly.getId())
                .orElseThrow(() -> new IdNotFoundException(Tables.FLY.name()))));

        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> hotels.put(hotelRepository.findById(hotel.getId())
                        .orElseThrow(() -> new IdNotFoundException(Tables.HOTEL.name())),
                hotel.getTotalDays()));

        var tourToPersist = TourEntity.builder()
                .tickets(tourHelper.createTickets(flights, customer))
                .reservations(tourHelper.createReservation(hotels, customer))
                .customer(customer)
                .build();

        var tourSaved = tourRepository.save(tourToPersist);

        customerHelper.incrase(customer.getDni(), TourServiceImp.class);

        if (Objects.nonNull(request.getEmail())) {
            this.emailHelper.sendMail(
                    request.getEmail(),
                    customer.getFullName(),
                    Tables.TOUR.name().toLowerCase()
            );
        }

        log.info("Tour saved with id: {}", tourSaved.getId());

        return TourResponse.builder()
                .id(tourSaved.getId())
                .reservationIds(tourSaved.getReservations()
                        .stream()
                        .map(ReservationEntity::getId)
                        .collect(Collectors.toSet()))
                .ticketIds(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .build();

    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = tourRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(Tables.TOUR.name()));

        return TourResponse.builder()
                .id(tourFromDb.getId())
                .reservationIds(tourFromDb.getReservations()
                        .stream()
                        .map(ReservationEntity::getId)
                        .collect(Collectors.toSet()))
                .ticketIds(tourFromDb.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void delete(Long id) {
        var tourToRemove = tourRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(Tables.TOUR.name()));

        tourRepository.delete(tourToRemove);
    }


    @Override
    public void removeTicket(Long tourId, UUID ticketId) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow(() -> new IdNotFoundException(Tables.FLY.name()));

        tourUpdate.removeTicket(ticketId);
        tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addTicket(Long tourId, Long flyId) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow(() -> new IdNotFoundException(Tables.TOUR.name()));

        var fly = flyRepository.findById(flyId)
                .orElseThrow(() -> new IdNotFoundException(Tables.FLY.name()));

        var ticket = tourHelper.createTicket(fly, tourUpdate.getCustomer());
        tourUpdate.addTicket(ticket);
        tourRepository.save(tourUpdate);
        return ticket.getId();
    }

    @Override
    public void removeReservation(Long tourId, UUID reservationId) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow(() -> new IdNotFoundException(Tables.TOUR.name()));

        tourUpdate.removeReservation(reservationId);
        tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addReservation(Long tourId, Long hotelId, Integer totalDays) {
        var tourUpdate = tourRepository.findById(tourId)
                .orElseThrow(() -> new IdNotFoundException(Tables.TOUR.name()));

        var hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IdNotFoundException(Tables.HOTEL.name()));

        var reservation = tourHelper.createReservation(hotel, tourUpdate.getCustomer(), totalDays);
        tourUpdate.addReservation(reservation);
        return reservation.getId();
    }
}
