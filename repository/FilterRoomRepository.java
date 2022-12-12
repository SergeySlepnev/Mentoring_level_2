package com.spdev.repository;

import com.spdev.entity.Room;
import com.spdev.filter.RoomFilter;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface FilterRoomRepository {

    @EntityGraph(value = "roomContents")
    List<Room> findAllByFilter(RoomFilter filter, Integer pageSize);
}