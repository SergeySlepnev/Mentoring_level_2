package com.dmdev.unit;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.service.UserService;
import com.dmdev.util.LocalDateFormatter;
import com.dmdev.util.RandomUtils;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Tag("unit")
@Tag("service")
@DisplayName("Unit functionality of for service layer")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserDao userDao;
    @Mock
    private UserService userService;

    private final CreateUserDto createUserDto = CreateUserDto.builder()
            .name("name")
            .birthday("2010-10-12")
            .email("email@gmail.com")
            .password("encryptedWord")
            .role(Role.USER.name())
            .gender(Gender.MALE.name())
            .build();

    private final UserDto userDto = UserDto.builder()
            .id(RandomUtils.getId())
            .name("name")
            .birthday(LocalDateFormatter.format("2010-10-12"))
            .email("email@gmail.com")
            .image("Image")
            .role(Role.USER)
            .gender(Gender.MALE)
            .build();

    private final User user = User.builder()
            .name("name")
            .birthday(LocalDateFormatter.format("2010-10-12"))
            .email("email@gmail.com")
            .password("encryptedWord")
            .role(Role.USER)
            .gender(Gender.MALE)
            .build();

    @Test
    void shouldCreateNewUserInDataBase() {
        createUserValidator.validate(createUserDto);
        verify(createUserValidator, times(1)).validate(createUserDto);
        createUserMapper.map(createUserDto);
        verify(createUserMapper, times(1)).map(createUserDto);
        userDao.save(user);
        verify(userDao, times(1)).save(user);
        userMapper.map(user);
        verify(userMapper, times(1)).map(user);

        var createUserDtoArgumentCaptor = ArgumentCaptor.forClass(CreateUserDto.class);
        userService.create(createUserDto);
        verify(userService).create(createUserDtoArgumentCaptor.capture());

        var captorValue = createUserDtoArgumentCaptor.getValue();
        assertThat(captorValue.getName()).isEqualTo("name");
    }

    @Test
    void itShouldLoginByEmailAndPassword() {
        userDao.findByEmailAndPassword(anyString(), anyString());
        verify(userDao, times(1)).findByEmailAndPassword(anyString(), anyString());
        userMapper.map(user);
        verify(userMapper, times(1)).map(user);
        doReturn(Optional.of(userDto)).when(userService).login(anyString(), anyString());
        var maybeUser = userService.login(anyString(), anyString());

        assertThat(maybeUser).isPresent();
    }
}