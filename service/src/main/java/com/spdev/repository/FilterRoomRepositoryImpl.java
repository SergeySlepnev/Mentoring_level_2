package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.dto.RoomFilter;
import com.spdev.entity.Room;
import com.spdev.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import static com.spdev.entity.QRoom.room;

@RequiredArgsConstructor
public class FilterRoomRepositoryImpl implements FilterRoomRepository {

    private static final Integer DEFAULT_PAGE_INDEX = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 3;

    private final EntityManager entityManager;

    @Override
    public Page<Room> findAllByFilter(RoomFilter filter, Integer pageSize) {
        var predicate = QPredicates.builder()
                .add(filter.type(), room.type::eq)
                .add(filter.available(), room.available::eq)
                .add(filter.costFrom(), room.cost::goe)
                .add(filter.costTo(), room.cost::loe)
                .build();

        var result = new JPAQuery<Room>(entityManager)
                .select(room)
                .from(room)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), entityManager.getEntityGraph("Room.roomContents"))
                .orderBy(room.cost.asc())
                .fetch();

        var request = buildPageRequest(pageSize);
        return new PageImpl<>(result, request, result.size());
    }

    @Override
    public void update(Room hotel) {
        entityManager.merge(hotel);
        entityManager.flush();
    }

    private PageRequest buildPageRequest(Integer pageSize) {
        return pageSize != null
                ? PageRequest.of(DEFAULT_PAGE_INDEX, pageSize)
                : PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE);
    }
}