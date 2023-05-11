package com.aleyn.best_travel.api.models.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest implements Serializable {

    @NotBlank(message = "Id client is mandatory")
    @Size(min = 9, max = 20, message = "The size must be between 10 and 20 characters long")
    private String idClient;

    @Positive(message = "Must be greater than 0")
    @NotNull(message = "Id hotel is mandatory")
    private Long idHotel;

    @NotNull(message = "Total days is mandatory")
    @Min(value = 1, message = "Min one day to make reservation")
    @Max(value = 30, message = "Max 30 days to make reservation")
    private Integer totalDays;

    // @Pattern(regexp = "^(.+)@(.+)$") // Example
    @Email(message = "Invalid email")
    private String email;

}
