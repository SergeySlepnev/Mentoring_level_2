package com.spdev.service;

import com.spdev.dto.HotelDetailsCreateEditDto;
import com.spdev.dto.HotelDetailsReadDto;
import com.spdev.mapper.HotelDetailsCreateEditMapper;
import com.spdev.mapper.HotelDetailsReadMapper;
import com.spdev.repository.HotelDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HotelDetailsService {

    private final HotelDetailsRepository hotelDetailsRepository;
    private final HotelDetailsCreateEditMapper hotelDetailsCreateEditMapper;
    private final HotelDetailsReadMapper hotelDetailsReadMapper;

    public Optional<HotelDetailsReadDto> findByHotelId(Integer hotelId) {
        return hotelDetailsRepository.findByHotelId(hotelId)
                .map(hotelDetailsReadMapper::map);
    }

    @Transactional
    public HotelDetailsReadDto create(HotelDetailsCreateEditDto createDTO) {
        return Optional.of(createDTO)
                .map(hotelDetailsCreateEditMapper::map)
                .map(hotelDetailsRepository::save)
                .map(hotelDetailsReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<HotelDetailsReadDto> update(Integer id, HotelDetailsCreateEditDto hotelDetailsDto) {
        return hotelDetailsRepository.findByHotelId(id)
                .map(entity -> hotelDetailsCreateEditMapper.map(hotelDetailsDto, entity))
                .map(hotelDetailsRepository::saveAndFlush)
                .map(hotelDetailsReadMapper::map);
    }

    public List<String> findCountries() {
        return hotelDetailsRepository.findCountries();
    }
}