package com.aleyn.best_travel.api.controllers;

import com.aleyn.best_travel.api.models.responses.HotelResponse;
import com.aleyn.best_travel.infrastructure.abstract_services.HotelService;
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

@Tag(name = "Hotel")
@RestController
@AllArgsConstructor
@RequestMapping(path = "hotel")
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Returns all hotels in a paginated list ")
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
    public ResponseEntity<Page<HotelResponse>> getAll(
            @RequestParam Integer numPage,
            @RequestParam Integer sizePage,
            @RequestHeader(required = false, defaultValue = "NONE")SortType sortType) {

        var response = hotelService.readAll(numPage, sizePage, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns a list of hotels less than the provided price")
    @GetMapping(path = "less_price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(@RequestParam BigDecimal price) {
        var response = hotelService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns a list of hotels between than the provided prices")
    @GetMapping(path = "between_price")
    public ResponseEntity<Set<HotelResponse>> getBetweenPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        var response = hotelService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns a list of greater than provided rating")
    @Parameter(
            name = "sortType",
            description = "Hotel rating can be from 1 to 5",
            required = true,
            example = "3"
    )
    @GetMapping(path = "greater_rating")
    public ResponseEntity<Set<HotelResponse>> getGreaterRating(@RequestParam() Integer rating) {
        if (rating > 4) rating = 4;
        if (rating < 1) rating = 1;
        var response = hotelService.findRatingGreaterThan(rating);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
