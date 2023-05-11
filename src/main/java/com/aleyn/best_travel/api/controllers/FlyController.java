package com.aleyn.best_travel.api.controllers;

import com.aleyn.best_travel.api.models.responses.FlyResponse;
import com.aleyn.best_travel.infrastructure.abstract_services.FlyService;
import com.aleyn.best_travel.util.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

@Tag(name = "Fly")
@RestController
@AllArgsConstructor
@RequestMapping(path = "fly")
public class FlyController {

    private final FlyService flyService;

    @Operation(summary = "Returns all flights in a paginated list ")
    @Parameters({
            @Parameter(
                    name = "numPage",
                    description = "The page number",
                    required = true,
                    example = "1"
            ),
            @Parameter(
                    name = "sizePage",
                    description = "The page size",
                    required = true,
                    example = "10"),
            @Parameter(
                    name = "sortType",
                    description = "The sort order | values: LOWER, UPPER, NONE | default value = NONE",
                    required = false,
                    example = "LOWER"
            )
    })
    @GetMapping
    public ResponseEntity<Page<FlyResponse>> getAll(
            @RequestParam Integer numPage,
            @RequestParam Integer sizePage,
            @RequestHeader(required = false, defaultValue = "NONE") SortType sortType) {

        var response = flyService.readAll(numPage, sizePage, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns a list of flights less than the provided price")
    @GetMapping(path = "less_price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice(@RequestParam BigDecimal price){
        var response = flyService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns a list of flights between than the provided prices")
    @GetMapping(path = "between_price")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max){

        var response = flyService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns a list of flights having the same origin and destination")
    @GetMapping(path = "origin_destiny")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice(
            @RequestParam String origin,
            @RequestParam String destiny){

        var response = flyService.findByOriginDestiny(origin, destiny);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
