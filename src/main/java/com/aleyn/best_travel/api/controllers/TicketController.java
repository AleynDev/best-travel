package com.aleyn.best_travel.api.controllers;

import com.aleyn.best_travel.api.models.request.TicketRequest;
import com.aleyn.best_travel.api.models.responses.ErrorResponse;
import com.aleyn.best_travel.api.models.responses.TicketResponse;
import com.aleyn.best_travel.infrastructure.abstract_services.TicketService;
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
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Ticket")
@RestController
@AllArgsConstructor
@RequestMapping(path = "ticket")
public class TicketController {

    private final TicketService ticketService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @Operation(summary = "Save a Ticket in the system")
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.create(request));
    }

    @Operation(summary = "Return a Tour with the provided id")
    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.read(id));
    }

    @Operation(summary = "Return the flight price the provided id")
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getPriceFly(
            @RequestParam Long flyId,
            @RequestHeader(required = false) Currency currency
            ) {
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", ticketService.getPriceFly(flyId, currency)));
    }

    @Operation(summary = "update the Ticket of the provided id")
    @PutMapping(path = "{id}")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable UUID id,
            @Valid @RequestBody TicketRequest request
    ) {
        return ResponseEntity.ok(ticketService.update(request, id));
    }

    @Operation(summary = "Delete a Ticket with the provided id")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
