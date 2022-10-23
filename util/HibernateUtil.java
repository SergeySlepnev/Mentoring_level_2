package com.spdev.util;

import com.spdev.converter.CostConverter;
import com.spdev.entity.BookingRequest;
import com.spdev.entity.Hotel;
import com.spdev.entity.HotelContent;
import com.spdev.entity.HotelDetails;
import com.spdev.entity.Review;
import com.spdev.entity.ReviewContent;
import com.spdev.entity.Room;
import com.spdev.entity.RoomContent;
import com.spdev.entity.User;
import com.spdev.converter.RatingConverter;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        var configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Hotel.class)
                .addAnnotatedClass(HotelContent.class)
                .addAnnotatedClass(HotelDetails.class)
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(RoomContent.class)
                .addAnnotatedClass(BookingRequest.class)
                .addAnnotatedClass(Review.class)
                .addAnnotatedClass(ReviewContent.class);
        configuration.addAttributeConverter(new CostConverter(), true);
        configuration.addAttributeConverter(new RatingConverter(), true);

        return configuration;
    }
}