package com.spdev.repository;

import com.spdev.dto.RoomFilter;
import com.spdev.entity.Room;
import org.springframework.data.domain.Page;

public interface FilterRoomRepository {

    Page<Room> findAllByFilter(RoomFilter filter, Integer pageSize);

    void update(Room hotel);
}
