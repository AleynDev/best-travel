package com.aleyn.best_travel.infrastructure.helpers;

import com.aleyn.best_travel.domain.entities.jpa.*;
import com.aleyn.best_travel.domain.repositories.jpa.ReservationRepository;
import com.aleyn.best_travel.domain.repositories.jpa.TicketRepository;
import com.aleyn.best_travel.infrastructure.services.ReservationServiceImp;
import com.aleyn.best_travel.infrastructure.services.TicketServiceImp;
import com.aleyn.best_travel.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@Transactional
@AllArgsConstructor
public class TourHelper {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public Set<TicketEntity> createTickets(Set<FlyEntity> flights, CustomerEntity customer) {
        var response = new HashSet<TicketEntity>(flights.size());
        flights.forEach(fly -> {
            var ticketToPersist = TicketEntity.builder()
                    .id(UUID.randomUUID())
                    .fly(fly)
                    .customer(customer)
                    .price(fly.getPrice().add(fly.getPrice().multiply(TicketServiceImp.CHARGE_PRICE_PERCENTAGE)))
                    .purchaseDate(LocalDate.now())
                    .arrivalDate(BestTravelUtil.getRandomLater())
                    .departureDate(BestTravelUtil.getRandomSoon())
                    .build();

            response.add(ticketRepository.save(ticketToPersist));
        });
        return response;
    }

    public Set<ReservationEntity> createReservation(HashMap<HotelEntity, Integer> hotels, CustomerEntity customer) {
        var response = new HashSet<ReservationEntity>(hotels.size());
        hotels.forEach((hotel, totalDays) -> {
            var reservationToPersist = ReservationEntity.builder()
                    .id(UUID.randomUUID())
                    .hotel(hotel)
                    .customer(customer)
                    .dateTimeReservation(LocalDateTime.now())
                    .dateStart(LocalDate.now())
                    .dateEnd(LocalDate.now().plusDays(totalDays))
                    .totalDays(totalDays)
                    .price(hotel.getPrice()
                            .add(hotel.getPrice().multiply(ReservationServiceImp.CHARGE_PRICE_PERCENTAGE)))
                    .build();

            response.add(reservationRepository.save(reservationToPersist));
        });
        return response;
    }

    public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer) {
        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice()
                        .add(fly.getPrice().multiply(TicketServiceImp.CHARGE_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();

        return ticketRepository.save(ticketToPersist);

    }

    public ReservationEntity createReservation(HotelEntity hotel, CustomerEntity customer, Integer totalDays) {
        var reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now()) //
                .dateStart(LocalDate.now()) //
                .dateEnd(LocalDate.now().plusDays(totalDays)) //
                .totalDays(totalDays) //
                .price(hotel.getPrice()
                        .add(hotel.getPrice().multiply(ReservationServiceImp.CHARGE_PRICE_PERCENTAGE)))
                .build();

        return reservationRepository.save(reservationToPersist);
    }


}
