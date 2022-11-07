package com.spdev.repository;

import com.spdev.dto.HotelFilter;
import com.spdev.entity.Hotel;
import org.springframework.data.domain.Page;

public interface FilterHotelRepository {

    Page<Hotel> findAllByFilter(HotelFilter filter, Integer pageSize);

    //правильно ли сюда помещать этот метод, если он не использует фильтр?
    void update(Hotel hotel);
}
