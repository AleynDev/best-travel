package com.aleyn.best_travel.api.models.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourHotelRequest implements Serializable {

    @Positive(message = "Must be greater than 0")
    @NotNull(message = "Id hotel is mandatory")
    private Long id;

    @NotNull(message = "Total days is mandatory")
    @Min(value = 1, message = "Min one day to make reservation")
    @Max(value = 30, message = "Max 30 days to make reservation")
    private Integer totalDays;

}
