package com.spdev.dto;

import com.spdev.entity.enums.ContentType;
import com.spdev.validation.CheckContentType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HotelContentCreateDto {

    Integer hotelId;

    @CheckContentType(message = "{error.invalid_content_type}")
    MultipartFile link;

    ContentType contentType;
}