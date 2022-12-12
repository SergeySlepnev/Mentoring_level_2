package com.spdev.http.controller;

import com.spdev.dto.PageResponse;
import com.spdev.entity.enums.RoomType;
import com.spdev.entity.enums.Star;
import com.spdev.filter.RoomFilter;
import com.spdev.service.HotelDetailsService;
import com.spdev.service.HotelService;
import com.spdev.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/my-booking/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final HotelDetailsService hotelDetailsService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public String findAll(Model model,
                          RoomFilter filter,
                          Pageable pageable) {
        var page = roomService.findAll(filter, pageable);
        model.addAttribute("filter", filter);
        model.addAttribute("rooms", PageResponse.of(page));
        model.addAttribute("countries", hotelDetailsService.findCountries());
        model.addAttribute("localities", hotelDetailsService.findLocalities());
        model.addAttribute("stars", Star.values());
        model.addAttribute("types", RoomType.values());

        return "room/search";
    }

    @GetMapping("/{roomId}")
    public String findById(@PathVariable("roomId") Integer roomId,
                           Model model) {
        return roomService.findById(roomId)
                .map(room -> {
                    model.addAttribute("room", room);
                    return "room/room";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}