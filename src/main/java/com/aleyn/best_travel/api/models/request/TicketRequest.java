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
public class TicketRequest implements Serializable {

    @NotBlank(message = "Id client is mandatory")
    @Size(min = 9, max = 20, message = "The size must be between 10 and 20 characters long")
    private String idClient;

    @Positive(message = "Must be greater than 0")
    @NotNull(message = "Id fly is mandatory")
    private Long idFly;

    @Email(message = "Invalid email")
    private String email;

}
