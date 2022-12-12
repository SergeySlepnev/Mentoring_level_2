package com.spdev.repository;

import com.spdev.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RoomRepository extends
        JpaRepository<Room, Integer>,
        FilterRoomRepository,
        QuerydslPredicateExecutor<Room> {

}