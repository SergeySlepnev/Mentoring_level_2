package com.spdev.http.controller;

import com.spdev.dto.HotelContentCreateDto;
import com.spdev.dto.HotelCreateEditDto;
import com.spdev.dto.HotelDetailsCreateEditDto;
import com.spdev.dto.PageResponse;
import com.spdev.entity.enums.ContentType;
import com.spdev.entity.enums.Star;
import com.spdev.entity.enums.Status;
import com.spdev.filter.HotelFilter;
import com.spdev.service.HotelContentService;
import com.spdev.service.HotelDetailsService;
import com.spdev.service.HotelService;
import com.spdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final UserService userService;
    private final HotelDetailsService hotelDetailsService;
    private final HotelContentService hotelContentService;

    @GetMapping
    public String findAll(Model model, HotelFilter filter, Pageable pageable) {
        var page = hotelService.findAll(filter, pageable);
        model.addAttribute("hotels", PageResponse.of(page));
        model.addAttribute("stars", Star.values());
        model.addAttribute("filter", filter);
        return "hotel/hotels";
    }

    @GetMapping("/{hotelId}")
    public String findById(@PathVariable("hotelId") Integer id, Model model) {
        var maybeHotel = hotelService.findById(id);
        var maybeHotelDetails = hotelDetailsService.findByHotelId(id);

        if (maybeHotel.isPresent() && maybeHotelDetails.isPresent()) {
            maybeHotel.ifPresent(hotel -> model.addAttribute("hotel", hotel));
            maybeHotelDetails.ifPresent(hotelDetails -> model.addAttribute("hotelDetails", hotelDetails));
            hotelContentService.findByHotelId(id)
                    .ifPresent(hotelContent -> model.addAttribute("hotelContent", hotelContent));
            return "hotel/hotel";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/add-hotel")
    public String add(@PathVariable("userId") Integer id, Model model,
                      @ModelAttribute("hotel") HotelCreateEditDto hotel,
                      @ModelAttribute("hotelDetails") HotelDetailsCreateEditDto hotelDetails,
                      @ModelAttribute("hotelContent") HotelContentCreateDto hotelContent) {
        userService.findById(id).map(user -> model.addAttribute("user", user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("hotel", hotel);
        model.addAttribute("hotelDetails", hotelDetails);
        model.addAttribute("hotelContent", hotelContent);
        model.addAttribute("stars", Star.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("contentTypes", ContentType.values());

        return "hotel/add";
    }

    @PostMapping("/{userId}")
    public String create(@PathVariable("userId") Integer userId,
                         @ModelAttribute("hotel") @Validated HotelCreateEditDto hotelDTO,
                         BindingResult hotelBindingResult,
                         @ModelAttribute("hotelDetails") @Validated HotelDetailsCreateEditDto hotelDetailsCreateDTO,
                         BindingResult hotelDetailsBindingResult,
                         @ModelAttribute("hotelContent") HotelContentCreateDto hotelContentCreateDTO,
                         RedirectAttributes redirectAttributes) {
        if (hotelBindingResult.hasErrors() || hotelDetailsBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("hotel", hotelDTO);
            redirectAttributes.addFlashAttribute("hotelDetails", hotelDetailsCreateDTO);
            redirectAttributes.addFlashAttribute("hotelContent", hotelContentCreateDTO);
            redirectAttributes.addFlashAttribute("hotelErrors", hotelBindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("hotelDetailsErrors", hotelDetailsBindingResult.getAllErrors());
            return "redirect:/hotels/" + userId + "/add-hotel";
        }

        var hotelReadDTO = hotelService.create(hotelDTO);
        hotelDetailsCreateDTO.setHotelId(hotelReadDTO.getId());
        hotelContentCreateDTO.setHotelId(hotelReadDTO.getId());
        hotelDetailsService.create(hotelDetailsCreateDTO);
        if (hotelContentCreateDTO.getLink() != null && hotelContentCreateDTO.getContentType() != null) {
            hotelContentService.create(hotelContentCreateDTO);
        }

        return "redirect:/hotels/" + hotelReadDTO.getId();
    }
}