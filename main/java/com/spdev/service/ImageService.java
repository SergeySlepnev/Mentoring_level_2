package com.spdev.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@RequiredArgsConstructor
public class ImageService {

    //здесь с property не разобрался ещё, не работает ни в какую)
    private final String bucket = "C:\\Users\\LENOVO\\IdeaProjects\\Level_2\\dmdev-project-level-2\\images";

    @SneakyThrows
    public void upload(String imagePath, InputStream content) {
        var fullImagePath = Path.of(bucket, imagePath);

        try (content) {
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath, content.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<byte[]> get(String imagePath) {
        var fullImagePath = Path.of(bucket, imagePath);
        return Files.exists(fullImagePath)
                ? Optional.of(Files.readAllBytes(fullImagePath))
                : Optional.empty();
    }
}