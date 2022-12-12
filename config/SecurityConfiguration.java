package com.spdev.config;

import com.spdev.entity.User;
import com.spdev.repository.UserRepository;
import com.spdev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import static com.spdev.entity.enums.Role.ADMIN;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .antMatchers("/users/{\\d+}").permitAll()
                        .antMatchers("/my-booking/**", "/hotels", "/my-booking/{\\+d}/content",
                                "/hotels/{\\d+}/**", "/login", "/my-booking/registration",
                                "/users/create", "/hotels/{\\+d}/hotel-content/{\\+d}/delete",
                                "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .antMatchers("/users", "/users/{\\+d}/delete", "/users/{\\+d}/update").hasAuthority(ADMIN.getAuthority())
                        .antMatchers("/admin/**").hasAuthority(ADMIN.getAuthority())
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/my-booking"))
                .formLogin(login -> login
                        .loginPage("/my-booking")
                        .successHandler((request, response, authentication) -> userRepository.findByUsername(authentication.getName())
                                .ifPresent(user -> {
                                    //Нормально ли вот так добавлять пользователя в сессию? Это для того, чтобы в url добавить userId.
                                    request.getSession(true).setAttribute("authUser", user);
                                    redirectToUserPage(response, user);
                                })))
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/my-booking")
                        .successHandler((request, response, authentication) -> userRepository.findByUsername(authentication.getName())
                                .ifPresent(user -> {
                                    request.getSession(true).setAttribute("authUser", user);
                                    redirectToUserPage(response, user);
                                }))
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService())));

        return http.build();
    }

    @SneakyThrows
    private static void redirectToUserPage(HttpServletResponse response, User user) {
        try {
            response.sendRedirect("/users/" + user.getId());
        } catch (IOException exception) {
            throw new RuntimeException();
        }
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");
            UserDetails userDetails = userService.loadUserByUsername(email);
            OidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());

            Set<Method> userDetailsMethods = Set.of(UserDetails.class.getMethods());

            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[]{UserDetails.class, OidcUser.class},
                    (proxy, method, args) -> userDetailsMethods.contains(method)
                            ? method.invoke(userDetails, args)
                            : method.invoke(oidcUser, args));
        };
    }
}