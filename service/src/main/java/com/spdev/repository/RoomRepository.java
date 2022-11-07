package com.spdev.repository;

import com.spdev.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer>, FilterRoomRepository {

}