package com.spdev.service;

import com.spdev.entity.enums.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

//Этот класс не относится к сервисам и в нём нет @Transactional.
// В какой пакет его лучше убрать и как назвать, чтобы не вводил в заблуждение?
@Service
@RequiredArgsConstructor
public class ApplicationContentService {

    @Value("${spring.servlet.multipart.location}")
    private final String bucket;

    @Value("${spring.servlet.multipart.max-request-size}")
    private final String maxFileSize;

    @SneakyThrows
    public void uploadImage(String imagePath, InputStream content) {
        var fullImagePath = Path.of(bucket, imagePath);
        try (content) {
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath, content.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<byte[]> getContent(String imagePath) {
        var fullImagePath = Path.of(bucket, imagePath);
        return Files.exists(fullImagePath)
                ? Optional.of(Files.readAllBytes(fullImagePath))
                : Optional.empty();
    }

    @SneakyThrows
    public void delete(String contentPath) {
        var fullContentPath = Path.of(bucket, contentPath);
        if (Files.exists(fullContentPath)) {
            Files.delete(fullContentPath);
        }
    }

    @SneakyThrows
    public ContentType getType(String contentPath) {
        var path = Path.of(contentPath);
        var type = Files.probeContentType(path);
        return type.startsWith("image")
                ? ContentType.PHOTO
                : ContentType.VIDEO;
    }

    @SneakyThrows
    public boolean isContentValid(String contentPath) {
        var path = Path.of(contentPath);
        var type = Files.probeContentType(path);
        return type == null || type.startsWith("image") || type.startsWith("video");
    }

    //Этот метод тоже пока не работает
    @SneakyThrows
    public boolean isAllowedSize(String contentPath) {
        var path = Path.of(contentPath);
        var mBInString = maxFileSize.substring(0, maxFileSize.indexOf('M'));
        var file = new File(contentPath);
        var maxAllowSize = Long.parseLong(mBInString) * 1024 * 1024;
        return maxAllowSize > file.length();
    }
}