package com.spdev.http.controller;

import com.spdev.service.HotelContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/hotels/{hotelId}/content")
@RequiredArgsConstructor
public class HotelContentController {

    private final HotelContentService hotelContentService;

    @PostMapping("/{contentId}/delete")
    public String delete(@PathVariable("contentId") Integer contentId,
                         @PathVariable("hotelId") Integer hotelId) {
        if (!hotelContentService.delete(contentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/hotels/" + hotelId;
    }
}