package com.aleyn.best_travel.api.controllers;

import com.aleyn.best_travel.api.models.request.ReservationRequest;
import com.aleyn.best_travel.api.models.responses.ErrorResponse;
import com.aleyn.best_travel.api.models.responses.ReservationResponse;
import com.aleyn.best_travel.infrastructure.abstract_services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Tag(name = "Reservation")
@RestController
@AllArgsConstructor
@RequestMapping(path = "reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @Operation(summary = "Save a hotel reservation in the system")
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.create(request));
    }

    @Operation(summary = "Return a Reservation with the provided id")
    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.read(id));
    }

    @Operation(summary = "Return the reservation price the provided id")
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getPriceHotel(
            @RequestParam Long hotelId,
            @RequestHeader(required = false) Currency currency
            ) {
        if (Objects.isNull(currency)) currency = Currency.getInstance("USD");

        return ResponseEntity.ok(Collections.singletonMap(
                "hotelPrice", reservationService.getPriceHotel(hotelId, currency)));
    }

    @Operation(summary = "update the Reservation of the provided id")
    @PutMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable(name = "id") UUID idReservation, @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.update(request, idReservation));
    }

    @Operation(summary = "Delete a Reservation with the provided id")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
