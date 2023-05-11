package com.aleyn.best_travel.infrastructure.helpers;

import com.aleyn.best_travel.domain.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@AllArgsConstructor
public class CustomerHelper {

    private final CustomerRepository customerRepository;

    public void incrase(String customerId, Class<?> type) {
        var customerToUpdate = customerRepository.findById(customerId).orElseThrow();

        switch (type.getSimpleName()) {
            case "TourServiceImp" -> customerToUpdate.setTotalTours(customerToUpdate.getTotalTours() + 1);
            case "TicketServiceImp" -> customerToUpdate.setTotalFlights(customerToUpdate.getTotalFlights() + 1);
            case "ReservationServiceImp" -> customerToUpdate.setTotalLodgings(customerToUpdate.getTotalLodgings() + 1);
        }

        customerRepository.save(customerToUpdate);
    }

}