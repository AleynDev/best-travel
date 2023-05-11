package com.aleyn.best_travel.api.models.request;

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
public class TourFlyRequest implements Serializable {

    @Positive(message = "Must be greater than 0")
    @NotNull(message = "Id fly is mandatory")
    private Long id;

}
