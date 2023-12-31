package com.aleyn.best_travel.infrastructure.abstract_services;

import com.aleyn.best_travel.util.enums.SortType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Set;

public interface CatalogService<R> {

    Page<R> readAll(Integer numPage, Integer sizePage, SortType sortType);

    Set<R> readLessPrice(BigDecimal price);

    Set<R> readBetweenPrice(BigDecimal min, BigDecimal max);

    String FIELD_BY_SORT = "price";

}
