package com.spdev.dto;

import com.spdev.entity.enums.ContentType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HotelContentCreateDTO {

    Integer hotelId;
    MultipartFile link;
    ContentType contentType;
}