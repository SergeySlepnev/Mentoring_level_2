package com.spdev.integration.http.controller;

import com.spdev.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.spdev.dto.UserCreateEditDto.Fields.birthdate;
import static com.spdev.dto.UserCreateEditDto.Fields.firstName;
import static com.spdev.dto.UserCreateEditDto.Fields.image;
import static com.spdev.dto.UserCreateEditDto.Fields.lastName;
import static com.spdev.dto.UserCreateEditDto.Fields.phone;
import static com.spdev.dto.UserCreateEditDto.Fields.rawPassword;
import static com.spdev.dto.UserCreateEditDto.Fields.username;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends IntegrationTestBase {

    private static final String USER_1 = "1";

    private final MockMvc mockMvc;

    @Test
    void checkFindAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void checkFindById() throws Exception {
        mockMvc.perform(get("/users/" + USER_1))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("user/user"),
                        model().attributeExists("user"));
    }

    @Test
    void checkCreate() throws Exception {
        mockMvc.perform(post("/users")
                        .param(username, "TestEmail@gmail.com")
                        .param(rawPassword, "TestPassword")
                        .param(firstName, "TestFirstName")
                        .param(lastName, "TestLastName")
                        .param(birthdate, "2022-10-10")
                        .param(phone, "8-924-558-95-95")
                        .param(image, "TestPhotoLink.jpg")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );
    }

    @Test
    void checkRegistration() throws Exception {
        mockMvc.perform(get("/users/registration"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void checkUpdate() throws Exception {
        mockMvc.perform(post("/users/" + USER_1 + "/update")
                        .param(username, "UpdateEmail@gmail.com")
                        .param(rawPassword, "UpdatePassword")
                        .param(firstName, "UpdateFirstName")
                        .param(lastName, "UpdateLastName")
                        .param(birthdate, "2022-10-10")
                        .param(phone, "8-924-558-95-95")
                        .param(image, "UpdatePhotoLink.jpg")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/{\\d+}")
                );
    }

    @Test
    void checkDelete() throws Exception {
        mockMvc.perform(post("/users/" + USER_1 + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }
}