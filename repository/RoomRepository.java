package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.Hotel;
import com.spdev.entity.Room;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static com.spdev.entity.QRoom.room;

@Repository
public class RoomRepository extends RepositoryBase<Integer, Room> {

    public RoomRepository(EntityManager entityManager) {
        super(Room.class, entityManager);
    }

    public List<Room> findAllAvailableByHotel(String hotelName) {
        return new JPAQuery<Room>(getEntityManager())
                .select(room)
                .from(room)
                .where(room.hotel.name.eq(hotelName).and(room.available.eq(true)))
                .setHint(GraphSemantic.FETCH.getJpaHintName(), getEntityManager().getEntityGraph("withRoomContent"))
                .fetch();
    }

    public List<Room> findAllAvailableInHotelLocationOrderedAscByPrice(Hotel hotel) {
        return new JPAQuery<Room>(getEntityManager())
                .select(room)
                .from(room)
                .where(room.available.eq(true).and(room.hotel.hotelDetails.locality.eq(hotel.getHotelDetails().getLocality())))
                .orderBy(room.cost.asc())
                .fetch();
    }

    public List<Room> findAllAvailableWithCostFromToOrderedAscByCost(BigDecimal costFrom, BigDecimal costTo) {
        return new JPAQuery<Room>(getEntityManager())
                .select(room)
                .from(room)
                .where(room.cost.between(costFrom, costTo))
                .orderBy(room.cost.asc())
                .fetch();
    }
}