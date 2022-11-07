package com.spdev.repository;

import com.spdev.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer>, FilterHotelRepository {

}