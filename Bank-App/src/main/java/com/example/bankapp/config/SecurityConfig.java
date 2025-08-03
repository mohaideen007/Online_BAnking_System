package com.example.bankapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.bankapp.BankappApplication;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.service.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    


    @Autowired
    AccountService accountService;


    @Bean
    public static PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(s->s.disable())
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/register").permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(f-> f
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(l -> l
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                        )

                .headers(h->h.
                        frameOptions(frameOptions->frameOptions.sameOrigin())
                );


                return http.build();
}


@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(accountService).passwordEncoder(PasswordEncoder());




}

}
