package com.spdev.http.controller;

import com.spdev.dto.PageResponse;
import com.spdev.dto.UserCreateEditDto;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.Status;
import com.spdev.filter.UserFilter;
import com.spdev.service.HotelService;
import com.spdev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final HotelService hotelService;

    @GetMapping
    public String findAll(Model model, UserFilter filter, Pageable pageable) {
        var page = userService.findAll(filter, pageable);
        model.addAttribute("users", PageResponse.of(page));
        model.addAttribute("filter", filter);
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", Status.values());
        return "user/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id,
                           Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    model.addAttribute("userHotels", hotelService.findAllByOwner(id));
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("user") @Validated UserCreateEditDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/my-booking/registration";
        }
        userService.create(user);
        return "redirect:/my-booking";
    }

    @GetMapping("/{userId}/update")
    public String update(@PathVariable("userId") Integer userId,
                         @ModelAttribute("user") @Validated UserCreateEditDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/" + userId + "/update";
        }
        return "redirect:/users/" + userId;
    }

    @PostMapping("/{userId}/update")
    public String update(@PathVariable("userId") Integer userId,
                         @ModelAttribute("user") UserCreateEditDto user) {
        return userService.update(userId, user)
                .map(it -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.clearContext();
        return "redirect:/my-booking";
    }

    @PostMapping("{userId}/change-status")
    public String changeStatus(@PathVariable("userId") Integer userId,
                               Status status) {
        userService.changeStatus(status, userId);
        return "redirect:/users/" + userId;
    }
}