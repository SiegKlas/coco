package ru.michael.coco.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.michael.coco.user.UserEntity;
import ru.michael.coco.user.UserRepository;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> (UserDetails) userRepo.findByUsername(username).orElseThrow();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .authorizeHttpRequests(
                        (authz) -> authz
                                .requestMatchers(mvc.pattern("/files"), mvc.pattern("/files/upload")).hasRole("USER")
                                .requestMatchers(mvc.pattern("/h2-console/**")).permitAll()
                                .requestMatchers(mvc.pattern("/"), mvc.pattern("/**")).permitAll()
                ).formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")
                ).logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                ).csrf(
                        AbstractHttpConfigurer::disable
                ).headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        // csrf и headers чтоб spring security не ругался на h2 console
        return http.build();
    }
}
