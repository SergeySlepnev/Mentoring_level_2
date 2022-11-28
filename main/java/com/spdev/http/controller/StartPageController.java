package com.spdev.http.controller;

import com.spdev.dto.HotelContentCreateDto;
import com.spdev.dto.HotelCreateEditDto;
import com.spdev.dto.HotelDetailsCreateEditDto;
import com.spdev.entity.enums.ContentType;
import com.spdev.entity.enums.Star;
import com.spdev.entity.enums.Status;
import com.spdev.repository.HotelRepository;
import com.spdev.service.HotelContentService;
import com.spdev.service.HotelDetailsService;
import com.spdev.service.HotelService;
import com.spdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/MyBooking")
@RequiredArgsConstructor
public class StartPageController {

    private final HotelService hotelService;
    private final HotelRepository hotelRepository;
    private final UserService userService;
    private final HotelDetailsService hotelDetailsService;
    private final HotelContentService hotelContentService;

    //Нормально ли в контроллере вызывать метод из hotelRepository? В сервисе такого метода нет.
    @GetMapping
    public String findTopFiveHotels(Model model) {
        var topFiveHotels = hotelRepository.findTopFiveByRatingWithDetailsAndFirstPhoto();
        model.addAttribute("topFiveHotels", topFiveHotels);
        return "/start_page";
    }

    @GetMapping("/{hotelId}")
    public String findById(@PathVariable("hotelId") Integer id, Model model) {
        var maybeHotel = hotelService.findById(id);
        var maybeHotelDetails = hotelDetailsService.findByHotelId(id);

        if (maybeHotel.isPresent() & maybeHotelDetails.isPresent()) {
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
        userService.findAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(user -> model.addAttribute("user", user))
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
        if (hotelContentCreateDTO.getLink() != null & hotelContentCreateDTO.getContentType() != null) {
            hotelContentService.create(hotelContentCreateDTO);
        }

        return "redirect:/hotels/" + hotelReadDTO.getId();
    }

    //update пока не работает)
    @GetMapping("/{userId}/{hotelId}/update")
    public String update(@PathVariable("hotelId") Integer hotelId,
                         @PathVariable("userId") Integer userId,
                         @ModelAttribute("hotel") @Validated HotelCreateEditDto hotel,
                         BindingResult hotelBindingResult,
                         @ModelAttribute("hotelDetails") @Validated HotelDetailsCreateEditDto hotelDetails,
                         BindingResult hotelDetailsBindingResult,
                         @ModelAttribute("hotelContent") HotelContentCreateDto hotelContentCreateDTO,
                         RedirectAttributes redirectAttributes) {
        if (hotelBindingResult.hasErrors() || hotelDetailsBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("hotel", hotel);
            redirectAttributes.addFlashAttribute("hotelDetails", hotelDetails);
            redirectAttributes.addFlashAttribute("hotelContent", hotelContentCreateDTO);
            redirectAttributes.addFlashAttribute("hotelErrors", hotelBindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("hotelDetailsErrors", hotelDetailsBindingResult.getAllErrors());
            return "redirect:/hotels/{userId}/{hotelId}/update";
        }
        return "hotel/update";
    }

    @PostMapping("/{hotelId}/update")
    public String update(@PathVariable("hotelId") Integer hotelId,
                         @ModelAttribute("hotel") HotelCreateEditDto hotel,
                         @ModelAttribute("hotelDetails") HotelDetailsCreateEditDto hotelDetails) {
        if (hotelService.update(hotelId, hotel).isPresent() & hotelDetailsService.update(hotelId, hotelDetails).isPresent()) {
            return "redirect:/hotels/{hotelId}";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseBody
    @GetMapping(value = "/{id}/content", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findContent(@PathVariable("id") Integer id) {
        return hotelContentService.findContent(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}