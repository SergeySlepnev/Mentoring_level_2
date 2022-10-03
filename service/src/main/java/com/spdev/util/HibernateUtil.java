package com.spdev.util;

import com.spdev.entity.BookingRequest;
import com.spdev.entity.Hotel;
import com.spdev.entity.HotelContent;
import com.spdev.entity.HotelDetails;
import com.spdev.entity.Review;
import com.spdev.entity.ReviewContent;
import com.spdev.entity.Room;
import com.spdev.entity.RoomContent;
import com.spdev.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        var configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Hotel.class);
        configuration.addAnnotatedClass(HotelContent.class);
        configuration.addAnnotatedClass(HotelDetails.class);
        configuration.addAnnotatedClass(Room.class);
        configuration.addAnnotatedClass(RoomContent.class);
        configuration.addAnnotatedClass(BookingRequest.class);
        configuration.addAnnotatedClass(Review.class);
        configuration.addAnnotatedClass(ReviewContent.class);
        return configuration;
    }
}
