package com.aleyn.best_travel.api.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourRequest implements Serializable {

    @NotBlank(message = "Id customer is mandatory")
    @Size(min = 9, max = 20, message = "The size must be between 10 and 20 characters long")
    private String customerId;

    @Size(min = 1, message = "Min flights per tour = 1")
    private Set<TourFlyRequest> flights;

    @Size(min = 1, message = "Min hotels per tour = 1")
    private Set<TourHotelRequest> hotels;

    @Email(message = "Invalid email")
    private String email;

}
