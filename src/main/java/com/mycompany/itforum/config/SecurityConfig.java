package com.mycompany.itforum.config;

import com.mycompany.itforum.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth -> auth
                /*.antMatchers("/css/**")
                .permitAll()
                .antMatchers("/auth/**")
                .permitAll()*/
                .antMatchers("/moderator/**")
                .hasRole("MODERATOR")
                .anyRequest()
                .permitAll()
                )
                .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/perform_login")
                .defaultSuccessUrl("/", false)
                .failureUrl("/auth/login?error=true")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutRequestMatcher(
                        new AntPathRequestMatcher("/logout", "POST") //  .logoutUrl("/logout")
                )
                .logoutSuccessUrl("/auth/logout_success")
                .permitAll()
                )
                .exceptionHandling()
                .accessDeniedPage("/403");

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, MyUserDetailsService myUserDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
