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
        model.addAttribute("countries", hotelDetailsService.findCountries());
        return "hotel/hotels";
    }

    @GetMapping("/{hotelId}")
    public String findById(@PathVariable("hotelId") Integer id, Model model) {
        var maybeHotel = hotelService.findById(id);
        var maybeHotelDetails = hotelDetailsService.findByHotelId(id);
        if (maybeHotel.isPresent() && maybeHotelDetails.isPresent()) {
            maybeHotel.map(hotel -> model.addAttribute("hotel", hotel));
            maybeHotelDetails.map(hotelDetails -> model.addAttribute("hotelDetails", hotelDetails));
            model.addAttribute("hotelContent", hotelContentService.findContent(id));
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
        model.addAttribute("contentTypes", ContentType.values());

        return "hotel/add";
    }

    @PostMapping("/{userId}")
    public String create(@PathVariable("userId") Integer userId,
                         @ModelAttribute("hotel") @Validated HotelCreateEditDto hotelDTO,
                         BindingResult hotelBindingResult,
                         @ModelAttribute("hotelDetails") @Validated HotelDetailsCreateEditDto hotelDetailsCreateDTO,
                         BindingResult hotelDetailsBindingResult,
                         @ModelAttribute("hotelContent") @Validated HotelContentCreateDto hotelContentCreateDTO,
                         BindingResult hotelContentBindingResult,
                         RedirectAttributes redirectAttributes) {
        if (hotelBindingResult.hasErrors() || hotelDetailsBindingResult.hasErrors() || hotelContentBindingResult.hasErrors()) {
            addAttributes(hotelDTO,
                    hotelBindingResult,
                    hotelDetailsCreateDTO,
                    hotelDetailsBindingResult,
                    hotelContentCreateDTO,
                    hotelContentBindingResult,
                    redirectAttributes);
            return "redirect:/hotels/" + userId + "/add-hotel";
        }

        var hotelReadDTO = hotelService.create(hotelDTO, hotelDetailsCreateDTO, hotelContentCreateDTO);
        return "redirect:/hotels/" + hotelReadDTO.getId();
    }

    @GetMapping("{userId}/user-hotels/{hotelId}/edit")
    public String update(@PathVariable("hotelId") Integer hotelId,
                         @PathVariable("userId") Integer userId,
                         Model model) {
        userService.findById(userId).map(user -> model.addAttribute("user", user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        hotelService.findById(hotelId).map(hotel -> model.addAttribute("hotel", hotel))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        hotelDetailsService.findByHotelId(hotelId).map(details -> model.addAttribute("hotelDetails", details))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("hotelContent", hotelContentService.findContent(hotelId));

        model.addAttribute("stars", Star.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("contentTypes", ContentType.values());

        return "hotel/edit";
    }

    @PostMapping("/{userId}/user-hotels/{hotelId}/update")
    public String update(@PathVariable("userId") Integer userId,
                         @PathVariable("hotelId") Integer hotelId,
                         @ModelAttribute("hotel") @Validated HotelCreateEditDto hotelDto,
                         BindingResult hotelBindingResult,
                         @ModelAttribute("hotelDetails") @Validated HotelDetailsCreateEditDto hotelDetailsCreateDTO,
                         BindingResult hotelDetailsBindingResult,
                         @ModelAttribute("hotelContent") @Validated HotelContentCreateDto hotelContentCreateDTO,
                         BindingResult hotelContentBindingResult,
                         RedirectAttributes redirectAttributes) {
        if (hotelBindingResult.hasErrors() || hotelDetailsBindingResult.hasErrors() || hotelContentBindingResult.hasErrors()) {
            addAttributes(hotelDto,
                    hotelBindingResult,
                    hotelDetailsCreateDTO,
                    hotelDetailsBindingResult,
                    hotelContentCreateDTO,
                    hotelContentBindingResult,
                    redirectAttributes);
            return "redirect:/hotels/" + userId + "/user-hotels/" + hotelId + "/edit";
        }

        return hotelService.update(hotelId, hotelDto, hotelDetailsCreateDTO, hotelContentCreateDTO)
                .map(hotel -> "redirect:/hotels/" + hotel.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{hotelId}/delete")
    public String delete(@PathVariable("hotelId") Integer hotelId) {
        if (!hotelService.delete(hotelId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/my-booking";
    }

    private static void addAttributes(HotelCreateEditDto hotelDto,
                                      BindingResult hotelBindingResult,
                                      HotelDetailsCreateEditDto hotelDetailsCreateDTO,
                                      BindingResult hotelDetailsBindingResult,
                                      HotelContentCreateDto hotelContentCreateDTO,
                                      BindingResult hotelContentBindingResult,
                                      RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("hotel", hotelDto);
        redirectAttributes.addFlashAttribute("hotelDetails", hotelDetailsCreateDTO);
        redirectAttributes.addFlashAttribute("hotelContent", hotelContentCreateDTO);
        redirectAttributes.addFlashAttribute("hotelErrors", hotelBindingResult.getAllErrors());
        redirectAttributes.addFlashAttribute("hotelDetailsErrors", hotelDetailsBindingResult.getAllErrors());
        redirectAttributes.addFlashAttribute("hotelContentErrors", hotelContentBindingResult.getAllErrors());
    }
}