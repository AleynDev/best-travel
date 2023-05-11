package com.aleyn.best_travel.infrastructure.services;

import com.aleyn.best_travel.api.models.responses.FlyResponse;
import com.aleyn.best_travel.domain.entities.FlyEntity;
import com.aleyn.best_travel.domain.repositories.FlyRepository;
import com.aleyn.best_travel.infrastructure.abstract_services.FlyService;
import com.aleyn.best_travel.util.enums.SortType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FlyServiceImp implements FlyService {

    private final FlyRepository flyRepository;

    @Override
    public Page<FlyResponse> readAll(Integer numPage, Integer sizePage, SortType sortType) {
        PageRequest pageRequest = null;
        switch (sortType) {
            case NONE  -> pageRequest = PageRequest.of(numPage, sizePage);
            case LOWER -> pageRequest = PageRequest.of(numPage, sizePage, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(numPage, sizePage, Sort.by(FIELD_BY_SORT).descending());
        }

        return flyRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    public Set<FlyResponse> readLessPrice(BigDecimal price) {
        return flyRepository.selectLessPrice(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {
        return flyRepository.selectBetweenPrice(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> findByOriginDestiny(String origin, String destiny) {
        return flyRepository.selectOriginDestiny(origin, destiny)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }


    private FlyResponse entityToResponse(FlyEntity entity) {
        var response = new FlyResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

}
