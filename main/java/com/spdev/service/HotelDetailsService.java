package com.spdev.service;

import com.spdev.dto.HotelDetailsCreateEditDTO;
import com.spdev.dto.HotelDetailsReadDTO;
import com.spdev.mapper.HotelDetailsCreateEditMapper;
import com.spdev.mapper.HotelDetailsReadMapper;
import com.spdev.repository.HotelDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HotelDetailsService {

    private final HotelDetailsRepository hotelDetailsRepository;
    private final HotelDetailsCreateEditMapper hotelDetailsCreateEditMapper;
    private final HotelDetailsReadMapper hotelDetailsReadMapper;

    public Optional<HotelDetailsReadDTO> findByHotelId(Integer hotelId) {
        return hotelDetailsRepository.findBy(hotelId)
                .map(hotelDetailsReadMapper::map);
    }

    @Transactional
    public HotelDetailsReadDTO create(HotelDetailsCreateEditDTO createDTO) {
        return Optional.of(createDTO)
                .map(hotelDetailsCreateEditMapper::map)
                .map(hotelDetailsRepository::save)
                .map(hotelDetailsReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<HotelDetailsReadDTO> update(Integer id, HotelDetailsCreateEditDTO userDto) {
        return hotelDetailsRepository.findBy(id)
                .map(entity -> hotelDetailsCreateEditMapper.map(userDto, entity))
                .map(hotelDetailsRepository::saveAndFlush)
                .map(hotelDetailsReadMapper::map);
    }
}