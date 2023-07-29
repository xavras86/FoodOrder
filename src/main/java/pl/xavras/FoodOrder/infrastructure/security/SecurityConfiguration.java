package pl.xavras.FoodOrder.infrastructure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailService
    )
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    SecurityFilterChain securityEnabled(HttpSecurity http) throws Exception {
        return http
                .csrf((csrf) -> csrf.configure(http)) // or method reference
                .authorizeHttpRequests(requests -> requests
                                .requestMatchers("/login", "/error", "/images/error.png").permitAll()
//                        .requestMatchers(HttpMethod.DELETE).hasAuthority("ADMIN")
                                .requestMatchers("/**").hasAnyAuthority("OWNER", "CUSTOMER")
                )
                .formLogin(formLogin -> formLogin
//                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true))
                .logout(logout -> logout
//                        .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                )
                .build();
    }

//    @Bean
//    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
//    SecurityFilterChain securityEnabled(HttpSecurity http) throws Exception {
//        http.csrf()
//            .disable()
//            .authorizeHttpRequests()
//            .requestMatchers("/login", "/error").permitAll()
//            .requestMatchers("/**").hasAnyAuthority("OWNER")
//            .requestMatchers("/**").hasAnyAuthority("CUSTOMER")
//            .and()
//            .formLogin()
//            .permitAll()
//            .and()
//            .logout()
//            .logoutSuccessUrl("/login")
//            .invalidateHttpSession(true)
//            .deleteCookies("JSESSIONID")
//            .permitAll();
//
//        return http.build();
//    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
    SecurityFilterChain securityDisabled(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .permitAll();

        return http.build();
    }

}
