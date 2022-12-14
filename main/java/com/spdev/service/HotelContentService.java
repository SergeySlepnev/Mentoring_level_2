package com.spdev.service;

import com.spdev.dto.HotelContentCreateDto;
import com.spdev.dto.HotelContentReadDto;
import com.spdev.entity.HotelContent;
import com.spdev.mapper.HotelContentCreateMapper;
import com.spdev.mapper.HotelContentReadMapper;
import com.spdev.repository.HotelContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HotelContentService {

    private final ImageService imageService;
    private final HotelContentRepository hotelContentRepository;
    private final HotelContentCreateMapper hotelContentCreateMapper;
    private final HotelContentReadMapper hotelContentReadMapper;

    public Optional<HotelContentReadDto> findByHotelId(Integer id) {
        return hotelContentRepository.findByHotelId(id)
                .map(hotelContentReadMapper::map);
    }

    @Transactional
    public HotelContentReadDto create(HotelContentCreateDto createDTO) {
        return Optional.of(createDTO)
                .map(entity -> {
                    uploadImage(createDTO.getLink());
                    return hotelContentCreateMapper.map(entity);
                })
                .map(hotelContentRepository::save)
                .map(hotelContentReadMapper::map)
                .orElseThrow();
    }

    public Optional<byte[]> findContent(Integer id) {
        return hotelContentRepository.findByHotelId(id)
                .map(HotelContent::getLink)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }
}