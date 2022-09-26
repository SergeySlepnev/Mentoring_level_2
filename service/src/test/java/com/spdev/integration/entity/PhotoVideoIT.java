package com.spdev.integration.entity;

import com.spdev.entity.PhotoVideo;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.spdev.integration.util.TestEntityUtil.FIRST_VALID_PHOTO_VIDEO;
import static com.spdev.integration.util.TestEntityUtil.INVALID_PHOTO_VIDEO;
import static com.spdev.integration.util.TestEntityUtil.SECOND_VALID_PHOTO_VIDEO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Tag("Testing mapping functionality of PhotoVideo.class")
class PhotoVideoIT extends IntegrationTestBase {

    @Test
    void shouldSaveNewValidPhotoVideoInDb() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction in shouldSaveNewValidPhotoVideoInDb() was opened {}:", testTransaction);

            log.info("Test valid PhotoVideo entity is in a transient state {}:", TestEntityUtil.SECOND_VALID_PHOTO_VIDEO);
            var expectedId = session.save(TestEntityUtil.SECOND_VALID_PHOTO_VIDEO);

            assertThat(expectedId).isNotNull();
            assertThat(TestEntityUtil.SECOND_VALID_PHOTO_VIDEO.getId()).isEqualTo(expectedId);
            log.info("Test shouldSaveNewValidPhotoVideoInDb() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowSQLExceptionIfPhotoVideoInvalid() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var invalidPhotoVideo = INVALID_PHOTO_VIDEO;
            log.info("Test invalid PhotoVideo entity is in a transient state {}:", invalidPhotoVideo);

            assertThrows(ConstraintViolationException.class, () -> session.save(invalidPhotoVideo));
            log.info("Test shouldThrowSQLExceptionIfPhotoVideoInvalid is passed");
        }
    }

    @Test
    void shouldGetExistingPhotoVideo() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedPhotoVideo = session.get(PhotoVideo.class, FIRST_VALID_PHOTO_VIDEO.getId());

            assertThat(expectedPhotoVideo).isNotNull();
            assertThat(FIRST_VALID_PHOTO_VIDEO).isEqualTo(expectedPhotoVideo);
            log.info("Test shouldGetExistingPhotoVideo() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldGetNullIfPhotoVideoNotExisting() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedPhotoVideo = session.get(PhotoVideo.class, 10);

            assertThat(expectedPhotoVideo).isNull();
            log.info("Test shouldGetNullIfPhotoVideoNotExisting() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingPhotoVideo() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedPhotoVideo = session.get(PhotoVideo.class, FIRST_VALID_PHOTO_VIDEO.getId());
            expectedPhotoVideo.setHotelId(1);
            expectedPhotoVideo.setRoomId(1);
            expectedPhotoVideo.setReviewId(1);
            expectedPhotoVideo.setLink("NewPhoto.jpg");
            session.update(expectedPhotoVideo);
            var actualPhotoVideo = session.get(PhotoVideo.class, FIRST_VALID_PHOTO_VIDEO.getId());

            assertThat(actualPhotoVideo).isNotNull();
            assertThat(actualPhotoVideo).isEqualTo(expectedPhotoVideo);
            log.info("Test shouldUpdateExistingPhotoVideo() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionWhileUpdateIfPhotoVideoNotExist() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            assertThrows(TransientObjectException.class, () -> session.update(new PhotoVideo()));
            log.info("Test shouldTrowExceptionWhileUpdateIfPhotoVideoNotExist() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingPhotoVideo() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var firstExpectedPhotoVideo = session.save(SECOND_VALID_PHOTO_VIDEO);
            assertThat(firstExpectedPhotoVideo).isNotNull();
            session.delete(SECOND_VALID_PHOTO_VIDEO);
            var expectedPhotoVideo = session.get(PhotoVideo.class, SECOND_VALID_PHOTO_VIDEO.getId());

            assertThat(expectedPhotoVideo).isNull();
            log.info("Test shouldDeleteExistingPhotoVideo() is passed");
            session.getTransaction().commit();
        }
    }
}