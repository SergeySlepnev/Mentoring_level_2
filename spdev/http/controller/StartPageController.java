package com.spdev.http.controller;

import com.spdev.dto.UserCreateEditDto;
import com.spdev.entity.enums.Role;
import com.spdev.repository.HotelRepository;
import com.spdev.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/my-booking")
@RequiredArgsConstructor
public class StartPageController {

    private final HotelService hotelService;
    private final HotelRepository hotelRepository;

    @GetMapping
    public String findTopFiveHotels(Model model) {
        var topFiveHotels = hotelRepository.findTopFiveByRatingWithDetailsAndFirstPhoto();
        model.addAttribute("topFiveHotels", topFiveHotels);
        return "/start_page";
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") UserCreateEditDto user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user/registration";
    }

    @ResponseBody
    @GetMapping(value = "/{id}/content", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findContent(@PathVariable("id") Integer id) {
        return hotelService.findContent(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}