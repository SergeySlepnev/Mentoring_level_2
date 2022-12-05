package com.spdev.service;

import com.spdev.dto.ContentReadDto;
import com.spdev.dto.HotelContentCreateDto;
import com.spdev.dto.HotelContentReadDto;
import com.spdev.entity.HotelContent;
import com.spdev.mapper.ContentReadMapper;
import com.spdev.mapper.HotelContentCreateMapper;
import com.spdev.mapper.HotelContentReadMapper;
import com.spdev.repository.HotelContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HotelContentService {

    private final ContentService contentService;
    private final HotelContentRepository hotelContentRepository;
    private final HotelContentCreateMapper hotelContentCreateMapper;
    private final HotelContentReadMapper hotelContentReadMapper;
    private final ContentReadMapper contentReadMapper;

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

    @Transactional
    public Optional<HotelContentReadDto> update(Integer id, HotelContentCreateDto hotelContentDto) {
        return hotelContentRepository.findById(id)
                .map(entity -> hotelContentCreateMapper.map(hotelContentDto, entity))
                .map(hotelContentRepository::saveAndFlush)
                .map(hotelContentReadMapper::map);
    }

    public List<ContentReadDto> findContent(Integer hotelId) {
        return hotelContentRepository.findByHotelId(hotelId)
                .stream()
                .map(contentReadMapper::map)
                .toList();
    }

    public void deleteContent(Integer hotelId) {
        var byHotelId = hotelContentRepository.findByHotelId(hotelId);
        for (HotelContent hotelContent : byHotelId) {
            var link = hotelContent.getLink();
            contentService.delete(link);
        }
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            contentService.uploadImage(image.getOriginalFilename(), image.getInputStream());
        }
    }
}