package com.spdev.service;

import com.spdev.dto.HotelContentCreateDto;
import com.spdev.dto.HotelCreateEditDto;
import com.spdev.dto.HotelDetailsCreateEditDto;
import com.spdev.dto.HotelInfo;
import com.spdev.dto.HotelReadDto;
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
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.spdev.entity.QHotel.hotel;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelReadMapper hotelReadMapper;
    private final HotelCreateEditMapper hotelCreateEditMapper;
    private final ContentService contentService;
    private final HotelDetailsService hotelDetailsService;
    private final HotelContentService hotelContentService;

    public Page<HotelReadDto> findAll(HotelFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.country(), hotel.hotelDetails.country::containsIgnoreCase)
                .add(filter.locality(), hotel.hotelDetails.locality::containsIgnoreCase)
                .add(filter.star(), hotel.hotelDetails.star::eq)
                .build();

        return hotelRepository.findAll(predicate, pageable)
                .map(hotelReadMapper::map);
    }

    public Optional<HotelReadDto> findById(Integer id) {
        return hotelRepository.findById(id)
                .map(hotelReadMapper::map);
    }

    @Transactional
    public HotelReadDto create(HotelCreateEditDto hotelDTO,
                               HotelDetailsCreateEditDto hotelDetailsDto,
                               HotelContentCreateDto hotelContentDto) {
        return Optional.of(hotelDTO)
                .map(hotelCreateEditMapper::map)
                .map(hotelRepository::save)
                .map(hotelReadMapper::map)
                .map(hotel -> {
                    hotelDetailsDto.setHotelId(hotel.getId());
                    hotelDetailsService.create(hotelDetailsDto);
                    if (hotelContentDto.getLink() != null) {
                        hotelContentDto.setHotelId(hotel.getId());
                        hotelContentService.create(hotelContentDto);
                    }
                    return hotel;
                }).orElseThrow();
    }

    @Transactional
    public Optional<HotelReadDto> update(Integer id, HotelCreateEditDto hotelDTO,
                                         HotelDetailsCreateEditDto hotelDetailsDto,
                                         HotelContentCreateDto hotelContentDto) {
        return hotelRepository.findById(id)
                .map(entity -> hotelCreateEditMapper.map(hotelDTO, entity))
                .map(hotelRepository::saveAndFlush)
                .map(hotelReadMapper::map)
                .map(hotel -> {
                    hotelDetailsService.update(hotel.getId(), hotelDetailsDto);
                    if (hotelContentDto.getLink() != null && hotelContentDto.getContentType() != null) {
                        hotelContentDto.setHotelId(hotel.getId());
                        hotelContentService.create(hotelContentDto);
                    }
                    return Optional.of(hotel);
                })
                .orElseThrow();
    }

    @Transactional
    public boolean delete(Integer id) {
        return hotelRepository.findById(id)
                .map(entity -> {
                    hotelContentService.deleteContent(entity.getId());
                    hotelRepository.delete(entity);
                    hotelRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public Optional<byte[]> findContent(Integer hotelId) {
        return hotelRepository.findTopFiveByRatingWithDetailsAndFirstPhoto()
                .stream()
                .filter(hotelInfo -> hotelInfo.id().equals(hotelId))
                .findFirst()
                .map(HotelInfo::link)
                .filter(StringUtils::hasText)
                .flatMap(contentService::getImage);
    }
}