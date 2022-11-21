package com.spdev.service;

import com.spdev.dto.HotelCreateEditDTO;
import com.spdev.dto.HotelReadDTO;
import com.spdev.filter.HotelFilter;
import com.spdev.mapper.HotelCreateEditMapper;
import com.spdev.mapper.HotelReadMapper;
import com.spdev.querydsl.QPredicates;
import com.spdev.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.spdev.entity.QHotel.hotel;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelReadMapper hotelReadMapper;
    private final HotelCreateEditMapper hotelCreateEditMapper;

    public Page<HotelReadDTO> findAll(HotelFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.country(), hotel.hotelDetails.country::containsIgnoreCase)
                .add(filter.locality(), hotel.hotelDetails.locality::containsIgnoreCase)
                .add(filter.star(), hotel.hotelDetails.star::eq)
                .build();

        return hotelRepository.findAll(predicate, pageable)
                .map(hotelReadMapper::map);
    }

    public Optional<HotelReadDTO> findById(Integer id) {
        return hotelRepository.findById(id)
                .map(hotelReadMapper::map);
    }

    @Transactional
    public HotelReadDTO create(HotelCreateEditDTO hotelDTO) {
        return Optional.of(hotelDTO)
                .map(hotelCreateEditMapper::map)
                .map(hotelRepository::save)
                .map(hotelReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<HotelReadDTO> update(Integer id, HotelCreateEditDTO hotelDTO) {
        return hotelRepository.findById(id)
                .map(entity -> hotelCreateEditMapper.map(hotelDTO, entity))
                .map(hotelRepository::saveAndFlush)
                .map(hotelReadMapper::map);
    }
}