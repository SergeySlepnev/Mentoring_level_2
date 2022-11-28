package com.spdev.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.dto.HotelInfo;
import com.spdev.entity.Hotel;
import com.spdev.filter.HotelFilter;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QHotel.hotel;

@RequiredArgsConstructor
public class FilterHotelRepositoryImpl implements FilterHotelRepository {

    private static final String FIND_TOP_FIVE = """
            SELECT DISTINCT h.id,
                            h.name,
                            hd.star,
                            FIRST_VALUE(hc.link) OVER (PARTITION BY h.id) link,
                            hc.type,
                            AVG(r.rating) avg_rating
            FROM hotel h
                     JOIN review r ON h.id = r.hotel_id
                     JOIN hotel_details hd ON h.id = hd.hotel_id
                     LEFT JOIN hotel_content hc ON h.id = hc.hotel_id
            WHERE hc.type = 'PHOTO'
               OR hc.link IS NULL
            GROUP BY h.id, h.name, hd.star, hc.link, hc.type
            ORDER BY avg_rating DESC
            LIMIT 5;
            """;

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    //link и type могут быть null, но метод rs.getString() в этом случае
    //возвращает null и ошибки нет. Стоит ли метод rs.getString() делать null safe
    // если да, то как правильно это сделать? rs.getString(Optional.ofNullable("link").orElse( что должно быть здесь?))
    @Override
    public List<HotelInfo> findTopFiveByRatingWithDetailsAndFirstPhoto() {
        return jdbcTemplate.query(FIND_TOP_FIVE,
                (rs, rowNum) -> new HotelInfo(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("star"),
                        rs.getString("link"),
                        rs.getString("type"),
                        rs.getDouble("avg_rating")
                ));
    }

    @Override
    public List<Hotel> findAllByFilter(HotelFilter filter, Predicate predicate) {
        return new JPAQuery<Hotel>(entityManager)
                .select(hotel)
                .from(hotel)
                .where(hotel.available.eq(true))
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), entityManager.getEntityGraph("Hotel.hotelDetails"))
                .orderBy(hotel.hotelDetails.star.asc())
                .fetch();
    }
}