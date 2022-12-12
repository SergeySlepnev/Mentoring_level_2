package com.spdev.service;

import com.spdev.dto.RoomReadDto;
import com.spdev.filter.RoomFilter;
import com.spdev.mapper.HotelCreateEditMapper;
import com.spdev.mapper.HotelReadMapper;
import com.spdev.mapper.RoomReadMapper;
import com.spdev.querydsl.QPredicates;
import com.spdev.repository.HotelRepository;
import com.spdev.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.spdev.entity.QHotel.hotel;
import static com.spdev.entity.QRoom.room;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomService {

    private final HotelRepository hotelRepository;
    private final HotelReadMapper hotelReadMapper;
    private final HotelCreateEditMapper hotelCreateEditMapper;
    private final ApplicationContentService applicationContentService;
    private final HotelDetailsService hotelDetailsService;
    private final HotelContentService hotelContentService;
    private final RoomRepository roomRepository;
    private final RoomReadMapper roomReadMapper;

    public Page<RoomReadDto> findAll(RoomFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.country(), hotel.hotelDetails.country::eq)
                .add(filter.locality(), hotel.hotelDetails.locality::eq)
                .add(filter.star(), hotel.hotelDetails.star::eq)
                .add(filter.type(), room.type::eq)
                .add(filter.costFrom(), room.cost::goe)
                .add(filter.costTo(), room.cost::loe)
                .add(filter.adultBedCount(), room.adultBedCount::eq)
                .add(filter.childrenBedCount(), room.childrenBedCount::eq)
                .build();

        return roomRepository.findAll(predicate, pageable)
                .map(roomReadMapper::map);
    }

    public Optional<RoomReadDto> findById(Integer id) {
        return roomRepository.findById(id)
                .map(roomReadMapper::map);
    }
}